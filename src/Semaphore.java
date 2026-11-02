public class Semaphore {
    protected int permits;
   
    protected Semaphore(){
        this.permits = 0;
    }
    protected Semaphore(int permits){
        this.permits = permits;
    }

    protected synchronized void aquire(){  
              
        while(permits<=0){ 
             try { wait() ; } catch( InterruptedException e ) {
            Thread.currentThread().interrupt(); 
            
        }
    }
 permits--;}
        
    
    
    protected synchronized void release(){
        permits++;
        this.notify();
    }
    
    protected synchronized boolean tryAquire (long timeoutMillis){
        long endTime = System.currentTimeMillis() + timeoutMillis;
        long remainingTime;

         while(permits<=0){ 
            remainingTime = endTime - System.currentTimeMillis();
            if (remainingTime <= 0){
                return false;
            }
              else {
            try {
                wait(remainingTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        
        }
           
        }
      
        permits--;
       return true;
    }
}
