# multiagent-jade

Create multiple agents (AgentSmith) with the help of the master agent (Architect)
to manipulate agents against a specific target.

This work is a part of my Lab class. 

![image](https://user-images.githubusercontent.com/55482580/139578534-e68fc74b-e38d-4f72-acd9-cb22ea46b5e2.png)

# Part I: The agents attack (Client-side)


![image](https://user-images.githubusercontent.com/55482580/140186678-29ea85a2-03bd-455e-a15c-fcc5045d5597.png)

Figure 1:

Figure 1 shows the Gui interface for the coordinator agent. Coordinator agent takes the following parameters from the user via Gui:
●	Number of Agents: Number of agent smiths to be launched
●	Session Time: The ticker time, time after which same request is sent again
●	Message: Nth term of fin
●	IP: IP address for the TCP server
●	Port: Port number for the TCP server
Once the attack button is pressed, the coordinator agent creates the requested number of agents. Each agent sends a request to the TCP server after every session time interval to calculate the Nth term of fibonacci sequences using the iterative algorithm.
Agent Console shows the log of the requests that are sent and the replies that are received.

![image](https://user-images.githubusercontent.com/55482580/140186710-0de984a1-2438-4d4d-8ee5-2d0241d4544f.png)

Figure 2:

Figure 2 shows the log of the TCP server. It displays the requests that are received and the replies that are sent back.


![image](https://user-images.githubusercontent.com/55482580/140186733-df16eac2-6363-4048-b5f6-3a019c930121.png)

Figure 3: AWS instance running TCP server

![image](https://user-images.githubusercontent.com/55482580/140186752-c520f7e0-66a0-487b-929f-5e45d0246f28.png) 

Figure 4: Jade agent management gui showing all the launched agents


![image](https://user-images.githubusercontent.com/55482580/140186770-96be2b41-0467-4dc1-ab12-7835db20710f.png)

Figure 5: Sniffer agent showing the communication between the agents and architect


# Part II: No Pasaran (Server-side)

1.	We created an EC2 instance on AWS and implemented a multi-threaded TCP Server in Java. From that instance, we created an AMI image to create a launch template. Since we did not deploy any Server Platform(Apache Server, etc), we need to run a java file manually. We can make the process automatic by adding the user data to the template. Whenever a new instance is created, it runs this user data first.


![image](https://user-images.githubusercontent.com/55482580/140186834-1477865f-bc26-4ebf-995f-98d80884476f.png)


Figure 6: Launch template


2.	Creating a load balancer for TCP traffic on the port 80.


![image](https://user-images.githubusercontent.com/55482580/140186857-b82d9636-9b91-4b9b-9c6b-2f3b3af60166.png)


Figure 7: Load Balancer


3.	A target group is created for TCP port 80. 


![image](https://user-images.githubusercontent.com/55482580/140186874-9fb2aa8e-cec0-4237-915c-87204835f205.png)


Figure 8: Target Group

4.	Specifying the group size for AutoScaling


![image](https://user-images.githubusercontent.com/55482580/140186904-556bad38-4987-4ae0-a3d8-bdfaa0e116be.png)


Figure 9: Group size for AutoScaling


5.	Scaling Policy based on CPU utilization


![image](https://user-images.githubusercontent.com/55482580/140186949-32cb5caf-febe-449f-9b53-4c641cab8034.png)


Figure 9: Target tracking policy


6.	Before starting the attack (only one instance)


![image](https://user-images.githubusercontent.com/55482580/140186967-712f20eb-4597-48d8-966d-10401353f3f0.png)


Figure 10: Instances for Autoscaling group


7.	Agents start attacking


![image](https://user-images.githubusercontent.com/55482580/140186989-48af9531-5e2d-4a1c-8f6f-6784738f7761.png) 


Figure 11: Architect GUI 


8.	Checking the CPU utilization of the first instance


![image](https://user-images.githubusercontent.com/55482580/140187002-fa374e5a-9a4e-4b0a-b76e-e16d5de767b1.png)


Figure 12: The default instance CPU utilization 


9.	Additional 3 instances are added due to the increase in the CPU utilization of the first instance


![image](https://user-images.githubusercontent.com/55482580/140187031-2ee00415-b5fa-4786-8c3f-33a05a7a9172.png)


Figure 13: All the instances


10.	Checking the AutoScaling group (4 instances)


![image](https://user-images.githubusercontent.com/55482580/140187050-adbe0d0d-6cd0-4de9-95e2-3122fbc04386.png)


Figure 13: AutoScaling group



# Part III: The war of worlds

1.	The AWS EC2 instance that was created for TCP Server can handle no more than 2000 sockets at a time. When we attacked with 4000 agents, it ran out of the resources and did not respond to even any primitive commands (cd, ls etc.) 

2.	When the CPU utilization of the default instances reaches 50% (in our case), AWS platform forecasts the increase pattern of it and based on the forecast, it creates a specific number of additional instances.

3.	Another solution would be creating a load balancer based on the network traffic so that the instances cannot run out of their resources.

4.	The server capacity to handle requests also depends on the requested message to calculate a fibonacci number. If the requested number is low, it takes less time to calculate and the server can handle more client requests.
