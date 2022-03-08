package less5;

public class TestThreads {
    static final int SIZE = 1500000;
    static final int HALF_SIZE = SIZE / 2;
    float[] testArray = new float[SIZE];

    public static void main(String[] args) {
        long a;
        TestThreads testThreads1 = new TestThreads();
        TestThreads testThreads2 = new TestThreads();
        testThreads1.fillArray(testThreads1.testArray, SIZE);
        testThreads2.fillArray(testThreads2.testArray, SIZE);

        // Run a calculation in 1 thread "main"
        {
            a = System.currentTimeMillis();
            testThreads1.fillArrayExpresion(testThreads1.testArray, SIZE);
            System.out.println(">>>Время работы в один поток: " + (System.currentTimeMillis() - a) + "ms");
        }

        //divide array[SIZE] on 2
        a = System.currentTimeMillis();
        float[] arrayThread1 = new float[HALF_SIZE];
        System.arraycopy(testThreads2.testArray, 0, arrayThread1, 0, HALF_SIZE);
        float[] arrayThread2 = new float[HALF_SIZE];
        System.arraycopy(testThreads2.testArray, HALF_SIZE, arrayThread2, 0, HALF_SIZE);

        // create 2 threads
        Thread t1 = new Thread(() -> {
            testThreads2.fillArrayExpresion(arrayThread1, HALF_SIZE);
        }, "Дочерний поток 1");

        Thread t2 = new Thread(() -> {
            testThreads2.fillArrayExpresion(arrayThread2, HALF_SIZE);
        }, "Дочерний поток 2");

        //start calculation in 2 threads
        t1.start();
        t2.start();

        try {
            t1.join();
            System.out.println(t1.getName() + " завершен: " + (System.currentTimeMillis() - a) + "ms");
            t2.join();
            System.out.println(t2.getName() + " завершен: " + (System.currentTimeMillis() - a) + "ms");
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Поток ПРЕРВАН!!!");
        }
        //gluing 2 arrays onto one array
        float[] resultCalculation = new float[SIZE];
        System.arraycopy(arrayThread1, 0, resultCalculation, 0, HALF_SIZE);
        System.arraycopy(arrayThread2, 0, resultCalculation, HALF_SIZE, HALF_SIZE);
        System.out.println(">>>Время работы в два потока: " + (System.currentTimeMillis() - a + "ms"));
        System.out.println("Основной поток завершен!");

    }

    void fillArray(float[] array, int length) {
        for (int i = 0; i < length; i++) {
            array[i] = 1;
        }
    }

    void fillArrayExpresion(float[] array, int length) {
        for (int i = 0; i < length; i++) {
            array[i] = (float) (array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            ;
        }
    }


}