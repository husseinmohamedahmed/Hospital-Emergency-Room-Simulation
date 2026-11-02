/*Doctor class*/
/*doctor_class*/
import java.util.Random;

public class Doctor extends Thread
{
    private int ID;
    private boolean Doc_availiable = true;
    private Patient p;
    private waitingRoom w;
    private treatmentRoom t;
    Random r = new Random();

    public Doctor(waitingRoom w, int ID)
    {
        this.w = w;
        this.ID = ID;
    }

    public int getDocID()
    { return ID;}

    public void run()
    {
        while(true)
        {
            //check if any patient avaliable in the waiting room
            if(w.checkWaitingRoom())
            {
                
                System.out.println("Patient " + p.getPatientID()+ " assigned to doctor "+ getDocID());
                p = w.takepatient(); //get patient from waiting room
                Doc_availiable = false;

                //try to acquire treatment room
                //TBD

                if(t.availableTR())//if treatment room aquired
                {
                    int random_Time = r.nextInt(5);
                    //wait a random time from 0 seconds to 5 seconds
                    try { wait(random_Time*1000) ; } catch( InterruptedException e ) { }

                    //doctor treated patient and will release treatment room
                    //TBD and ask how to acquire treatment room as doctor and release it
                    
                    t.releaseTR();
                    System.out.println("Patient " + p.getPatientID()+ "has been treated by doctor "+ getDocID())
                    Doc_availiable = true;

                }
                else
                {
                    w.returnpatient(p);
                    Doc_availiable = true;
                }
            }
            if(!w.checkWaitingRoom){ break; }//if there is no more patients in waiting room,
            //we exit the loop
        }
    }
}
