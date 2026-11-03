/*Doctor class*/
/*doctor_class*/
import java.util.Random;

public class Doctor extends Thread
{
    private int ID;
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
                
                p = w.takepatient(); //get patient from waiting room

                if(p == null)  {break;}//exit loop if patient is null

                System.out.println("Patient " + p.getPatientID()+ " assigned to doctor "+ getDocID());

                //try to acquire treatment room

                if(s.tryAcquire(3000))//if treatment room available to aquire
                {
                    
                    int random_Time = r.nextInt(6);
                    //wait a random time from 0 seconds to 5 seconds
                    Thread.sleep(random_Time * 1000)
                    
                    s.release();//doctor finished treating patient and releases the treatment room
                    System.out.println("Patient " + p.getPatientID()+ "has been treated by doctor "+ getDocID());
                    System.out.println("Doctor " + getDocID()+ "is now available");
                    p.getTreatmentDone().release();

                }
                else //failed to aquire treatment room
                {
                    System.out.println("Doctor "+ getDocID() + "could not get aquire treatment room, returnig patient " +p.getPatientID()+ " to waiting room.");
                    w.returnpatient(p);
                }
            }
        }
    }
}
