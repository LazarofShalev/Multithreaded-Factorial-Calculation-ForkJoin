
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FactorialCalculationForkJoin {

	final static int NUM = 20;

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();
		System.out.print(NUM + "! = " + parallel() + "\n");
		long endTime = System.currentTimeMillis();

		System.out.println("Number of processors is: " + Runtime.getRuntime().availableProcessors());
		System.out.println("Time: " + (endTime - startTime) + " milliseconds");

	}

	public static Long parallel() {
		RecursiveTask<Long> task = new MyTask(NUM);
		ForkJoinPool pool = new ForkJoinPool();
		return pool.invoke(task);
	}

	public static class MyTask extends RecursiveTask<Long> {
		private static final long serialVersionUID = 1L;

		private static long threshold = 2 * NUM / (Runtime.getRuntime().availableProcessors());;
		private long low;
		private long high;

		public MyTask(long num) {
			this(0, num);
		}

		private MyTask(long low, long high) {
			this.low = low;
			this.high = high;
		}

		@Override
		protected Long compute() {

			if (high - low <= threshold) {

				long fact = 1;

				for (long i = low + 1; i <= high; i++) {
					fact = fact * i;
				}
				return fact;
			}

			long mid = (low + high) / 2;

			RecursiveTask<Long> t1 = new MyTask(low, mid);
			RecursiveTask<Long> t2 = new MyTask(mid, high);

			invokeAll(t1, t2);

			return new Long(t1.join() * t2.join());
		}
	}

}
