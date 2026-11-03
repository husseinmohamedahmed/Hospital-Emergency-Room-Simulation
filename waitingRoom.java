

public class waitingRoom{
    // waiting room capacity
    private int size;
    

   
    private  Patient store[];
    // where we will insert 1st patient in waiting room 
    private int inptr=0;
    //where we will remove 1st patient
    private int outptr=0;
    private int count=0;// number of patients in waiting room


    private Semaphore spaces;//number of chairs available
    private Semaphore elements;//number of patients in waiting room
    private Semaphore mutex;//made to prevent race condition
    private Patient outside[];//patients outside the waiting room
    private int outsideIn=0;//where we will insert  patient outside the waiting room to stand in line 
    private int outsideOut=0;// removal 
    private int outsideCount=0;//total number of patients out in the line 


    //constructor

    public waitingRoom(int size,int totalPatients){
        this.size=size;
        store=new Patient[size];//patients number in waiting room
        spaces=new Semaphore(size);//how many chairs
        elements=new Semaphore(0);// no patients 
        mutex=new Semaphore(1);//doctor can see 1 patient
        outside=new Patient[totalPatients];//patient in line out waiting to entre waiting room
    }


    public void enterWaitingRoom(Patient patient){
        //avoid el critical section(prevent 2 patient to enter or do same thing at same time)

        try {
          mutex.acquire();//only 1 have lock and rest wait until it finishes
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return;
        }

        
        boolean hasSpace = false;
        if (outsideCount == 0) {
          try {
            hasSpace = spaces.tryAcquire(0);
          } catch (InterruptedException e) {
            mutex.release();
            Thread.currentThread().interrupt();
            return;
          }
        }

        if (outsideCount > 0 || !hasSpace){
            //if the line have patients waiting 
            outside[outsideIn]=patient;//put the patient in the line
            outsideIn=(outsideIn+1)%outside.length;//% if we are in end go to front again as it is circular array
            outsideCount++;// increment total el patients in line 
            System.out.println("the waiting rooom is full");
            System.out.println(patient + " is waiting outside the waiting room");
        }
   

        else{
            store[inptr]=patient;// put the patient in the waiting room 
            inptr = (inptr +1) % size ;
            count++;// tottal pepole in room increased
            System.out.println(patient +" entered in the waiting room");
            elements.release();//to say you have a patient 
        }
        mutex.release();//close of critical section  of mutex.P

        }


      
        public Patient takepatient(){
         Patient patient;
          try {
            elements.acquire();//wait until you have a patient
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
          }
          try {
            mutex.acquire();//to know how many patient will he take to avoid critical situation
          } catch (InterruptedException e) {
            elements.release();
            Thread.currentThread().interrupt();
            return null;
          }
          patient = store[outptr];//take the patient from the waiting room
          store[outptr]=null;//empty the place to avoid dangling pointer
          outptr=(outptr + 1) %size;//to go to the next patient in the waiting room 
          count--; //dec the tot no of patient

          
          // entre new patient from el line to waiting room 
          if (outsideCount > 0) {
            store[inptr] = outside[outsideOut]; // take from el line and put in waiting room 
            outside[outsideOut] = null; 
            inptr = (inptr + 1)%size;//move 1 step in room 
            outsideOut = (outsideOut + 1)%outside.length; //move 1 step in lin e
            outsideCount--; //dec count in lin e
            System.out.println("A patient from outside entered the waiting room");
            elements.release();         
          } 
          else{
            // no one stand in the line 
            spaces.release();//to say you have a chair available
            System.out.println(" i have an empty chair in room ");

          } 
          mutex.release();
          return patient;// go to tratment room with doc 

          }

           
          public void returnpatient(Patient patient)
          {
            enterWaitingRoom(patient);//go to the function we did up 
          }

          public int getPat_waitingcount() {
            // to prevent race condition 

            try {
              mutex.acquire();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return count;
            }
            int currentCount = count;
            mutex.release();
            return currentCount;
          }

          //get the number of patients waiting outside in line 
          public int getPat_outsidecount() {
            // to prevent race condition 
            try {
              mutex.acquire();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return outsideCount;
            }
            int currentCount = outsideCount;
            mutex.release();
            return currentCount;
          }


    }


