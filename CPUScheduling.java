import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

// a process class: used to store each process information
class Process{
    String id;
    int arrival, burst, completion, waiting, turnaround, remaining;

    // the Process constructor is used when user input a new process
    Process (String id, int arrival, int burst) {
        this.id = id;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
    }
}

public class CPUScheduling {
    
    // this function is used to handle process's input from the user 
    static ArrayList<Process> inputProcess() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();
        int arrival = 0, burst = 0;

        System.out.print("Enter a number of processes: ");
        int processNum = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Handle invalid input (negative or zero processes)
        while (processNum <= 0) {
            System.out.println("Invalid input! Number of processes must be greater than 0.");
            System.out.print("Enter a valid number of processes: ");
            processNum = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        }
        
        for (int i = 0; i < processNum; i++) {
            System.out.println("\nProcess " + (i + 1) + ": ");
            System.out.print("   - Process ID: ");
            String id = scanner.nextLine();

            // this loop is use to keep prompting the user to input only positive number for arrival time and burst time (adding the = 0 condition too)
            while (true) {
                try {
                    System.out.print("   - Arrival Time: ");
                    arrival = scanner.nextInt();
                    if (arrival < 0) throw new IllegalArgumentException("Arrival time must be positive");
                    System.out.print("   - Burst Time: ");
                    burst = scanner.nextInt();
                    if (burst <= 0) throw new IllegalArgumentException("Burst time must be positive");
                    scanner.nextLine();
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            processes.add(new Process(id, arrival, burst));
        }
        return processes;
    }
    
    // *** main function ***
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // this while loop is used to prompt user to select an option to perform and calculate the CPU scheduling until they exit
        while (true) {
            clearScreen();
            System.out.println("\nCPU Scheduling Algorithms: ");
            System.out.println("\t1. First-Come, First-Served(FCFS)");
            System.out.println("\t2. Shortest-Job First(SJF)");
            System.out.println("\t3. Shortest Remaining-Time(SRT)");
            System.out.println("\t4. Round Robin(RR)");
            System.out.println("\t5. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    clearScreen();
                    System.out.println("\n\t1. First-Come, First-Served(FCFS)\n");
                    fcfs(inputProcess());
                    System.out.print("Press any key to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                }
                case 2 -> {
                    clearScreen();
                    System.out.println("\n\t2. Shortest-Job First(SJF)\n");
                    sjf(inputProcess());
                    System.out.print("Press any key to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                }
                case 3 -> {
                    clearScreen();
                    System.out.println("\n\t3. Shortest Remaining-Time(SRT)\n");
                    srt(inputProcess());
                    System.out.print("Press any key to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                }
                case 4 -> {
                    clearScreen();
                    System.out.println("\n\t4. Round Robin(RR)\n");
                    System.out.print("Input quantum: ");
                    int quantum = scanner.nextInt();
                    roundRobin(inputProcess(), quantum);
                    System.out.print("Press any key to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                }
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> {
                    System.out.println("\nInvalid input! (1-5)");
                    System.out.print("Press any key to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                }
            }
        }
    }

    // *** function for each algorithm ***

    // First-Come First-Served function
    static void fcfs (ArrayList<Process> processes) {
        processes.sort(Comparator.comparing(process -> process.arrival));
        int time = 0;
        System.out.println("\n\t > Gantt Chart: ");
        
        for (int i = 0; i < processes.size(); i++) {
            Process p = processes.get(i);
            time = Math.max(time, p.arrival);
            int start = time;
            time += p.burst;
            
            // Print without arrow for the last process
            if (i == processes.size() - 1) {
                System.out.print(p.id + " (" + start + "–" + time + ")");
            } else {
                System.out.print(p.id + " (" + start + "–" + time + ") → ");
            }
            p.completion = time;
        }
        System.out.println(" ");
        displayResults(processes);
    }

    // Shortest-Job First function
    static void sjf(ArrayList<Process> processes) {
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrival));
    
        int time = 0; // Current time
        int completed = 0; // Number of completed processes
        ArrayList<Process> readyQueue = new ArrayList<>(); // Processes that have arrived and are ready to execute
    
        System.out.println("\nGantt Chart:");
    
        while (completed < processes.size()) {
            // Add processes to the ready queue that have arrived by the current time
            for (Process p : processes) {
                if (p.arrival <= time && !readyQueue.contains(p) && p.completion == 0) {
                    readyQueue.add(p);
                }
            }
    
            if (!readyQueue.isEmpty()) {
                // Sort ready queue by burst time (shortest burst first)
                readyQueue.sort(Comparator.comparingInt(p -> p.burst));
    
                // Get the process with the shortest burst time
                Process current = readyQueue.remove(0);
    
                int start = time;
                time += current.burst;
    
                // Print without an arrow for the last process
                if (completed == processes.size() - 1) {
                    System.out.print(current.id + " (" + start + "–" + time + ")");
                } else {
                    System.out.print(current.id + " (" + start + "–" + time + ") → ");
                }
                current.completion = time;
                completed++;
            } else {
                // If no process is ready to execute, move time forward
                time++;
            }
        }
        System.out.println(" | ");
        displayResults(processes);
    }

    // Shortest Remaining Time function
    public static void srt(List<Process> processes) {
        int n = processes.size();
        int time = 0, completed = 0;
        Process current = null;
        StringBuilder ganttChart = new StringBuilder("Gantt Chart: ");
        List<String> ganttEntries = new ArrayList<>();

        while (completed < n) {
            // Select the process with the shortest remaining time at current time
            Process shortest = null;
            for (Process p : processes) {
                if (p.arrival <= time && p.remaining > 0) {
                    if (shortest == null || p.remaining < shortest.remaining) {
                        shortest = p;
                    }
                }
            }

            if (shortest != null) {
                if (current != shortest) {
                    if (current != null && !ganttEntries.isEmpty()) {
                        ganttEntries.set(ganttEntries.size() - 1, ganttEntries.get(ganttEntries.size() - 1) + time + ")");
                    }
                    ganttEntries.add(shortest.id + " (" + time + " - ");
                }
                
                shortest.remaining--;
                if (shortest.remaining == 0) {
                    shortest.completion = time + 1;
                    shortest.turnaround = shortest.completion - shortest.arrival;
                    shortest.waiting = shortest.turnaround - shortest.burst;
                    completed++;
                }
                current = shortest;
                time++;
            } else {
                // If no process is available, move time forward (handle idle time properly)
                if (!ganttEntries.isEmpty() && !ganttEntries.get(ganttEntries.size() - 1).contains("Idle")) {
                    ganttEntries.add("Idle (" + time + " - ");
                }
                time++;
            }
        }

        // Close the last Gantt Chart entry
        if (!ganttEntries.isEmpty()) {
            ganttEntries.set(ganttEntries.size() - 1, ganttEntries.get(ganttEntries.size() - 1) + time + ")");
        }

        ganttChart.append(String.join(" -> ", ganttEntries));
        System.out.println(ganttChart);
        displayResults(processes);
    }

    // Round Robin function
    static void roundRobin(List<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>();
        List<Process> tempList = new ArrayList<>(processes);
        tempList.sort(Comparator.comparingInt(p -> p.arrival)); // Sort by arrival time

        int time = 0;
        queue.add(tempList.get(0));
        tempList.remove(0);

        System.out.println("\nGantt Chart:");
        while (!queue.isEmpty()) {
            Process current = queue.poll();
            int start = time;
            int executeTime = Math.min(current.remaining, quantum);
            time += executeTime;
            current.remaining -= executeTime;

            System.out.print(current.id + " (" + start + "–" + time + ")");
            if (!queue.isEmpty() || !tempList.isEmpty() || current.remaining > 0) {
                System.out.print(" → ");
            }

            // Add newly arrived processes to the queue
            List<Process> toAdd = new ArrayList<>();
            for (Process p : tempList) {
                if (p.arrival <= time) {
                    toAdd.add(p);
                }
            }
            tempList.removeAll(toAdd); // Remove them from the temporary list
            queue.addAll(toAdd); // Add them to the queue

            // If the current process is not finished, put it back in the queue
            if (current.remaining > 0) {
                queue.add(current);
            } else {
                current.completion = time;
            }
        }

        System.out.println("|");
        displayResults(processes);
    }

    // Display results
    static void displayResults(List<Process> processes) {
        int totalWaiting = 0, totalTurnaround = 0;
    
        // Header with correct column widths
        System.out.println("\nProcess  Arrival  Burst  Completion  Turnaround  Waiting");
    
        for (Process p : processes) {
            p.turnaround = p.completion - p.arrival;
            p.waiting = p.turnaround - p.burst;
    
            totalWaiting += p.waiting;
            totalTurnaround += p.turnaround;
    
            // Print each row with correct alignment
            System.out.printf("%-8s %-8d %-6d %-12d %-10d %-8d\n",
                    p.id, p.arrival, p.burst, p.completion, p.turnaround, p.waiting);
        }
    
        // Calculate and display averages with 2 decimal places
        double avgWaiting = (double) totalWaiting / processes.size();
        double avgTurnaround = (double) totalTurnaround / processes.size();
    
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaiting);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaround);
        
    }

    // Clearscreen function
    static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}