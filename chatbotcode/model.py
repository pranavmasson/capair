import torch
import torch.nn as nn


class NeuralNet(nn.Module):
    def __init__(self, input_size, hidden_size, num_classes):
        """
         Initialize neural network. This is called by constructor to initialize the neural network. You can override this if you want to do something other than setting the parameters of the neural network.
         
         @param input_size - size of the input layer. It is the number of features
         @param hidden_size - size of the hidden layer.
         @param num_classes - number of classes of the neural network
        """
        super(NeuralNet, self).__init__()
        self.l1 = nn.Linear(input_size, hidden_size) 
        self.l2 = nn.Linear(hidden_size, hidden_size) 
        self.l3 = nn.Linear(hidden_size, num_classes)
        self.relu = nn.ReLU()
    
    def forward(self, x):
        """
         Forward pass for neural network. This is the first step in the forward pass of the network.
         
         @param x - input tensor of shape [ batch_size in_channels ]
         
         @return output tensor of shape [ batch_size out_channels out_height out_width ] where out_channels is the output of the last layer
        """
        out = self.l1(x)
        out = self.relu(out)
        out = self.l2(out)
        out = self.relu(out)
        out = self.l3(out)
        # no activation and no softmax at the end
        return out