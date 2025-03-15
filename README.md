# CPU-Scheduling-Simulator

**PROJECT DESCRIPTION**
This program is written in Java and impletment the 4 **CPU Scheduling algorithms** including: 
1. **First-Come First-Served (FCFS)** an algorithms that executes processes in the order they arrive, with the first process arriving first being executed first
2. **Shortest-Job-First (SJF)** where it selects the process with the shortest burst time, considering both arrival time and burst time
3. **Shortest-Remaining-Time (SRT)** is a preemptive version of SJF, where the process with the shortest remaining burst time is executed next, and the CPU may interrupt (preempt) a running process if a shorter one arrives 
4. **Round Robin (RR)** gives CPU time equally among processes, each getting a fixed time slice(quantum).

The program simulates the execution of processes in a CPU scheduling scenario. It calculates various metrics such as:
  - **Gantt Chart:** A visual representation of the scheduling order.
  - **Waiting Time:** The waiting time for each process.
  - **Turnaround Time:** The turnaround time for each process.
  - **Average Waiting Time:** The average waiting time for all processes.
  - **Average Turnaround Time:** The average turnaround time for all processes.

**Instructions on how to compile and run the program**
  - **Step 01:** Clone the repository into your local machine
  - **Step 02:** open terminal and compile the java file using this command:
    **javac CPUScheduling.java**
  - **Step 03:** After the file is compiled, you can run the program using this command down below
    **java CPUScheduling**
    This will execute the program and you will see the output which firstly it prompts to input the type of algorithm then ask to input the number of processes and each process information including its ID, arrial time and burst time.
          
