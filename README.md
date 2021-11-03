# multiagent-jade

Create multiple agents (AgentSmith) with the help of the master agent (Architect)
to manipulate agents against a specific target.

This work is a part of my Lab class. 

![image](https://user-images.githubusercontent.com/55482580/139578534-e68fc74b-e38d-4f72-acd9-cb22ea46b5e2.png)


# Part I: The agents attack (Client-side)

Figure 1:

Figure 1 shows the Gui interface for the coordinator agent. Coordinator agent takes the following parameters from the user via Gui:
Number of Agents: Number of agent smiths to be launched
Session Time: The ticker time, time after which same request is sent again
Message: Nth term of fin
IP: IP address for the TCP server
Port: Port number for the TCP server
Once the attack button is pressed, the coordinator agent creates the requested number of agents. Each agent sends a request to the TCP server after every session time interval to calculate the Nth term of fibonacci sequences using the iterative algorithm.
Agent Console shows the log of the requests that are sent and the replies that are received.


Figure 2:

Figure 2 shows the log of the TCP server. It displays the requests that are received and the replies that are sent back.

Figure 3: AWS instance running TCP server


Figure 4: Jade agent management gui showing all the launched agents

 
Figure 5: Sniffer agent showing the communication between the agents and architect



# Part II: No Pasaran (Server-side)

We created an EC2 instance on AWS and implemented a multi-threaded TCP Server in Java. From that instance, we created an AMI image to create a launch template. Since we did not deploy any Server Platform(Apache Server, etc), we need to run a java file manually. We can make the process automatic by adding the user data to the template. Whenever a new instance is created, it runs this user data first.
 

Figure 6: Launch template

Creating a load balancer for TCP traffic on the port 80.


Figure 7: Load Balancer
A target group is created for TCP port 80. 


Figure 8: Target Group

Specifying the group size for AutoScaling


Figure 9: Group size for AutoScaling



Scaling Policy based on CPU utilization


Figure 9: Target tracking policy

Before starting the attack (only one instance)


Figure 10: Instances for Autoscaling group

Agents start attacking


Figure 11: Architect GUI 

Checking the CPU utilization of the first instance


Figure 12: The default instance CPU utilization 

Additional 3 instances are added due to the increase in the CPU utilization of the first instance


Figure 13: All the instances

Checking the AutoScaling group (4 instances)


Figure 13: AutoScaling group

# Part III: The war of worlds

The AWS EC2 instance that was created for TCP Server can handle no more than 2000 sockets at a time. When we attacked with 4000 agents, it ran out of the resources and did not respond to even any primitive commands (cd, ls etc.) 

When the CPU utilization of the default instances reaches 50% (in our case), AWS platform forecasts the increase pattern of it and based on the forecast, it creates a specific number of additional instances.

Another solution would be creating a load balancer based on the network traffic so that the instances cannot run out of their resources.

The server capacity to handle requests also depends on the requested message to calculate a fibonacci number. If the requested number is low, it takes less time to calculate and the server can handle more client requests.


