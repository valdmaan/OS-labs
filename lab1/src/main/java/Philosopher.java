public class Philosopher implements Runnable {
    private final Object leftFork;
    private final Object rightFork;

    public Philosopher(Object leftFork, Object rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        try {
            while (true) {
                doAction(System.nanoTime() + ": Thinking");
                synchronized (leftFork) {
                    doAction(
                        System.nanoTime()
                            + ": Picked up left fork");
                    synchronized (rightFork) {
                        doAction(
                            System.nanoTime()
                                + ": Picked up right fork - eating");

                        doAction(
                            System.nanoTime()
                                + ": Put down right fork");
                    }
                    doAction(
                        System.nanoTime()
                            + ": Put down left fork. Back to thinking");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void doAction(String action) throws InterruptedException {
        // введення потоку в сон на якусь кількість мілісек.
        System.out.println(
            Thread.currentThread().getName() + " " + action);
        Thread.sleep(((int) (Math.random() * 100)));
    }
}
