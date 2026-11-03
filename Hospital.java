
import java.util.Scanner;

public class Hospital {





    private static int checksInput(Scanner input, String question) {
        while (true) {
            System.out.print(question);

            int num = input.nextInt();
            if (num <= 0) {                         // handles the input if it is zero or negative number
                System.out.println("the number must be greater than zero");
                continue;
            }

            return num;
        }
    }

        public static void main (String[]args){


            Scanner input = new Scanner(System.in);
            int capacity = checksInput(input, "Waiting room capacity:");
            int numDoctors = checksInput(input, "Number of doctors:");
            int numPatients = checksInput(input, "Number of patients:");


            waitingRoom waitingRoom = new waitingRoom(capacity, numPatients);
            Semaphore treatmentRooms = new Semaphore(2);   // 2 treatment rooms        // in the requirements it must be 2 treatment rooms


           Doctor[] doctors = new Doctor[numDoctors];
            for (int i = 0; i < numDoctors; i++) {
                doctors[i] = new Doctor(i + 1, waitingRoom, treatmentRooms);
            }


            Patient[] patients = new Patient[numPatients];
            for (int i = 0; i < numPatients; i++) {
               patients[i] = new Patient(i + 1, waitingRoom);
            }

            System.out.println("Patients arriving:");
            for (int i = 1; i <= numPatients; i++) {
                System.out.print("P" + i +" ");
            }
            System.out.println();



        for (int i = 0; i < numDoctors; i++) {
        doctors[i].start();
    }
        for (int i = 0; i < numPatients; i++) {
                patients[i].start();
            }






            for (int i = 0; i < numPatients; i++) {
        try {
            patients[i].join();
        } catch (InterruptedException e) {
            System.out.println("patients join interruption");
        }
    }


            for (int i = 0; i < numDoctors; i++) {
                doctors[i].interrupt();
            }

        for (int i = 0; i < numDoctors; i++) {
        try {
            doctors[i].join();
        } catch (InterruptedException e) {
            System.out.println("doctors join interruption");
        }
    }


        System.out.println("All patients have been treated");
        System.out.println("Simulation completed");
}



}



