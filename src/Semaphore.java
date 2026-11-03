public class Semaphore {
    public int permits;
   
    public Semaphore(){
        this.permits = 0;
    }
    public Semaphore(int permits){
        this.permits = permits;
    }

    public synchronized void acquire() throws InterruptedException {
    while (permits <= 0) {
        wait();
    }
    permits--;
}
        
    
    
    public synchronized void release(){
        permits++;
        this.notify();
    }
    
public synchronized boolean tryAcquire(long timeoutMillis)
        throws InterruptedException {
    long endTime = System.currentTimeMillis() + timeoutMillis;

    while (permits <= 0) {
        long remainingTime = endTime - System.currentTimeMillis();

        if (remainingTime <= 0) {
            return false;
        }

        wait(remainingTime);
    }

    permits--;
    return true;
}
}
