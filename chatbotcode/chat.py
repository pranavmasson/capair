import random
import json
from flask import Flask, request, jsonify
from flask_cors import CORS

import torch
from model import NeuralNet
from nltk_utils import bag_of_words, tokenize

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})  # Allow CORS for all domains

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

with open('intents.json', 'r') as json_data:
    intents = json.load(json_data)

FILE = "data.pth"
data = torch.load(FILE)

input_size = data["input_size"]
hidden_size = data["hidden_size"]
output_size = data["output_size"]
all_words = data['all_words']
tags = data['tags']
model_state = data["model_state"]

model = NeuralNet(input_size, hidden_size, output_size).to(device)
model.load_state_dict(model_state)
model.eval()

bot_name = "Sam"

@app.route('/chat', methods=['POST'])
def chat():
    """
     Function to receive a message and return a response. This function is called by bot_request. py and should be the first function in your bot's function.
     
     
     @return JSON with the response of the bot. If there is no response it will return an empty JSON object
    """
    data = request.get_json()
    sentence = data['message']

    sentence = tokenize(sentence)
    X = bag_of_words(sentence, all_words)
    X = X.reshape(1, X.shape[0])
    X = torch.from_numpy(X).to(device)

    output = model(X)
    _, predicted = torch.max(output, dim=1)

    tag = tags[predicted.item()]

    probs = torch.softmax(output, dim=1)
    prob = probs[0][predicted.item()]

    # This function is used to generate a random response from the user s intent list
    if prob.item() > 0.75:
        # Returns a random response from the intent s responses.
        for intent in intents['intents']:
            # Returns a random response for the tag
            if tag == intent["tag"]:
                response = random.choice(intent['responses'])
                return jsonify({'bot_response': response})
    else:
        return jsonify({'bot_response': "I'm not sure I understand. Please check with our FAQs on our policy page for more assistance"})

# Run the app if the __main__ is called.
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8070)  # Run Flask app on all available network interfaces




