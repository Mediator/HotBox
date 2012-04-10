package hotbox;

import org.zeromq.ZMQ;

public class CommunicatorServer {

	private ZMQ.Context context;
	private ZMQ.Socket publisher;
	private final int port = 6382;
	public void initialize()
	{
		context = ZMQ.context(1);

        publisher = context.socket(ZMQ.PUB);
       
	}
	public void start()
	{
		 publisher.bind("tcp://*:" + port);
	     //publisher.bind("ipc:/hotbox");
	}
	
	public void publishCodeUpdate(String className, byte[] classData)
	{
		publisher.send("UPDATE".getBytes(), ZMQ.SNDMORE);
		publisher.send(className.getBytes(), ZMQ.SNDMORE);
		publisher.send(classData, 0);
	}
}
