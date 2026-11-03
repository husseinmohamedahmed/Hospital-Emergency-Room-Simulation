
public class Patient extends Thread {
    private int patientID;
    private waitingRoom waitingRoom;    
    private Semaphore treatmentDone;    

  public Patient(int patientID,waitingRoom waitingRoom){

      this.patientID = patientID;
      this.waitingRoom = waitingRoom;
      this.treatmentDone = new Semaphore(0); 

  }

  public int getPatientID(){
      return patientID;
  }

    @Override
    public String toString() {
        return "P" + patientID;
    }

  public Semaphore getTreatmentDone(){
      return treatmentDone;
  }

  @Override
    public void run(){
        System.out.println("P"+patientID+" arrived");

        waitingRoom.enterWaitingRoom(this);    

      try {
          treatmentDone.acquire();                             
      } catch (InterruptedException e) {
          System.out.println("error in patient's run method");
          return;
      }

      System.out.println("P"+patientID+" left");


  }



}
