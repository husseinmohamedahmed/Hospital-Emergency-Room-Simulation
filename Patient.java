
public class Patient extends Thread {
    private int patientID;
    private waitingRoom waitingRoom;    //habiba's class
    private Semaphore treatmentDone;    //hussein's class

  public Patient(int patientID,waitingRoom waitingRoom){

      this.patientID = patientID;
      this.waitingRoom = waitingRoom;
      this.treatmentDone = new Semaphore(0);  // zero means not treated yet

  }

  public int getPatientID(){
      return patientID;
  }

  public Semaphore getTreatmentDone(){
      return treatmentDone;
  }

  @Override
    public void run(){
        System.out.println("P"+patientID+" arrived");

        waitingRoom.enterWaitingRoom(this);    // from habiba's code

      try {
          treatmentDone.p();      //from semaphore--
      } catch (InterruptedException e) {
          System.out.println("error in patient's run method");
          return;
      }

      System.out.println("P"+patientID+" left");


  }



}
