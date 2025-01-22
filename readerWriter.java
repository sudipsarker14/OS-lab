package readerwriterproblem;
import java.util.concurrent.Semaphore;


/**
 *
 * @author sudip
 */
public class ReaderWriterProblem {
    private static final int NUM_READERS = 3;
    private static final int NUM_WRITERS = 1;
    private static Semaphore mutex = new Semaphore(1);
    private static Semaphore db = new Semaphore(1);
    private static int readCount = 0;
    
    static class Reader implements Runnable {
        private int id;

        public Reader(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    mutex.acquire();
                    readCount++;
                    if (readCount == 1) {
                        db.acquire();
                    }
                    mutex.release();

                    readDB();

                    mutex.acquire();
                    readCount--;
                    if (readCount == 0) {
                        db.release();
                    }
                    mutex.release();

                    Thread.sleep(1000); // Simulating some processing time
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void readDB() {
            System.out.println("Reader " + id + ": is reading the database");
        }
    }
    
    static class Writer implements Runnable {
        private int id;

        public Writer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    prepareData();
                    db.acquire();
                    writeData();
                    db.release();
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void prepareData() {
            System.out.println("Writer " + id + ": is preparing data");
        }

        private void writeData() {
            System.out.println("Writer " + id + ": is writing to the database");
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
         for (int i = 1; i <= NUM_READERS; i++) {
            new Thread(new Reader(i)).start();
        }

        for (int i = 1; i <= NUM_WRITERS; i++) {
            new Thread(new Writer(i)).start();
        }
    }
    
}
