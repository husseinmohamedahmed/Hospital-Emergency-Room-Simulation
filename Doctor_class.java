/*Doctor class*/
/*doctor_class*/
import java.util.Random;

public class Doctor extends Thread
{
    private int ID;
    private boolean Doc_availiable = true;
    private Patient p;
    private waitingRoom w;
    private Semaphore s;
    Random r = new Random();

    public Doctor(int ID, waitingRoom w, Semaphore s)
    {
        this.ID = ID;
        this.w = w;
        this.s = s;
    }

    public int getDocID()
    { return ID;}

    public void run()
    {
        while(true)
        {
            //check if any patient avaliable in the waiting room
            if(w.getPat_waitingcount() > 0)
            {
                
                System.out.println("Patient " + p.getPatientID()+ " assigned to doctor "+ getDocID());
                p = w.takepatient(); //get patient from waiting room
                Doc_availiable = false;

                //try to acquire treatment room

                if(s.tryaquire())//if treatment room available to aquire
                {
                    s.aquire();
                    int random_Time = r.nextInt(5);
                    //wait a random time from 0 seconds to 5 seconds
                    try { wait(random_Time*1000) ; } catch( InterruptedException e ) { }
                    
                    s.release();//doctor finished treating patient and releases the treatment room
                    System.out.println("Patient " + p.getPatientID()+ "has been treated by doctor "+ getDocID());
                    System.out.println("Doctor " + getDocID()+ "is now available");
                    Doc_availiable = true;

                }
                else //failed to aquire treatment room
                {
                    System.out.println("Doctor "+ getDocID() + "could not get aquire treatment room, returnig patient " +p.getPatientID()+ " to waiting room.");
                    w.returnpatient(p);
                    Doc_availiable = true;
                }
            }
            if((w.getPat_waitingcount() == 0) && (w.getPat_outsidecount() == 0)){ break; }//if there is no more patients in waiting room or outside,
            //we exit the loop
        }
    }
}
