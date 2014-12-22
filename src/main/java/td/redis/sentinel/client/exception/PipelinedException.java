package td.redis.sentinel.client.exception;

public class PipelinedException extends RuntimeException {

	public PipelinedException(String message) {
		super(message);
	}

}
