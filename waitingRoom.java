// el hy3tamed 3alia zy el doc lazem y3adel esm el clas l --->waitingRoom

public class waitingRoom{
    // waiting room capacity
    private int size;
    

    //chech mn hanya esm el class  <------
    private  patient store[];
    // where we will insert 1st patient in waiting room 
    private int inptr=0;
    //where we will remove 1st patient
    private int outptr=0;
    private int count=0;// number of patients in waiting room

    // check mn hussien  esm class el semaphore <------
    private semaphore spaces;//number of chairs available
    private semaphore elements;//number of patients in waiting room
    private semaphore mutex;//made to prevent race condition
    private patient outside[];//patients outside the waiting room
    private int outsideIn=0;//where we will insert  patient outside the waiting room to stand in line 
    private int outsideOut=0;// removal 
    private int outsideCount=0;//total number of patients out in the line 


    //constructor
    // check mn hussien  esm class el semaphore <------
    // check mn hanya esm el class  <------
    public waitingRoom(int size,int totalPatients){
        this.size=size;
        store=new patient[size];//patients number in waiting room
        spaces=new semaphore(size);//how many chairs
        elements=new semaphore(0);// no patients 
        mutex=new semaphore(1);//doctor can see 1 patient
        outside=new patient[totalPatients];//patient in line out waiting to entre waiting room
    }

    //check mn hanya esm el class  <------
    // entre waiting room handeling function
    public void enterWaitingRoom(patient Patient){
        //avoid el critical section(prevent 2 patient to enter or do same thing at same time)

        mutex.P();//only 1 have lock and rest wait until it finishes

        // t3deeeeel mhohem 3and hussien  fee el if
        // try p lazem tb2a fe el semaphore class de 8eer el lap bt3et el lab mesh bt7gez mkan low rag3 true f el requirment el matlob 3awez a7gez mkan 
        //so lazem nst3mel tryp badel spaces.P() 3ashan n3rf lw fe mkan wla la2 
        if (outsideCount>0 || !spaces.tryP(0)){
            //if the line have patients waiting 
            outside[outsideIn]=Patient;//put the patient in the line
            outsideIn=(outsideIn+1)%outside.length;//% if we are in end go to front again as it is circular array
            outsideCount++;// increment total el patients in line 
            System.out.println("the waiting rooom is full");
            System.out.println(Patient + " is waiting outside the waiting room");
        }
   
        // check mn hanya esm el class  <------
        // check mn hussien  esm el class el semaphore <------
        else{
            store[inptr]=Patient;// put the patient in the waiting room 
            inptr = (inptr +1) % size ;
            count++;// tottal pepole in room increased
            System.out.println(Patient +" entered in the waiting room");
            elements.V();//to say you have a patient 
        }
        mutex.V();//close of critical section  of mutex.P

        }


        // function to make doc take patient from el waiting room 
        //check mn hanya  esm el class  <------
        //check mn hussien  esm el class  <------
        public patient takepatient(){
          patient Patient;  
          elements.P();//wait until you have a patient
          mutex.P();//to know how many patient will he take to avoid critical situation
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
            System.out.println( " entered in the waiting room");
            elements.V(); // close .p =  there is a new patient ready for doc 
          } 
          else{
            // no one stand in the line 
            spaces.V();//to say you have a chair available
            System.out.println(" i have an empty chair in room ");

          } 
          mutex.V();
          return Patient;// go to tratment room with doc 

          }

          //check mn hanya  esm el class  <------
// mahmoud hyst5adem el method de el mafrood returnpatient 
          //function to return patient if there is no tratment room avaliabel 
          public void returnpatient(patient Patient)
          {
            enterWaitingRoom(Patient);//go to the function we did up 
          }

          public int getPat_waitingcount() {
            // to prevent race condition 


            mutex.P();
            int currentCount = count;
            mutex.V();
            return currentCount;
          }

          //get the number of patients waiting outside in line 
          public int getPat_outsidecount() {
            // to prevent race condition 
            mutex.P();
            int currentCount = outsideCount;
            mutex.V();
            return currentCount;
          }


    }


