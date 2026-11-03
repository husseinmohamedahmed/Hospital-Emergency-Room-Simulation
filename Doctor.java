
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

            {
                
                p = w.takepatient(); 

                if(p == null)  {break;}
                System.out.println("Patient " + p.getPatientID()+ " assigned to doctor "+ getDocID());

               

                boolean acquired;
                try
                {
                    acquired = s.tryAcquire(3000);
                }
                catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    return;
                }

                if(acquired)
                {
                    
                    int random_Time = r.nextInt(6);
                    
                    try
                    {
                        Thread.sleep(random_Time * 1000);
                    }
                    catch (InterruptedException e)
                    {
                        s.release();
                        Thread.currentThread().interrupt();
                        return;
                    }
                    
                    s.release();
                    System.out.println("Patient " + p.getPatientID()+ " has been treated by doctor "+ getDocID());
                    System.out.println("Doctor " + getDocID()+ " is now available");
                    p.getTreatmentDone().release();

                }
                else 
                {
                    System.out.println("Doctor "+ getDocID() + " could not get aquire treatment room, returnig patient " +p.getPatientID()+ " to waiting room.");
                    w.returnpatient(p);
                }
            }
        }
    }
}
