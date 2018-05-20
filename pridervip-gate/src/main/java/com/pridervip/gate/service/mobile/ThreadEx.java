package com.pridervip.gate.service.mobile;

public abstract class ThreadEx {

	private Thread thread;
	protected volatile boolean isRunning=false;
	
	public ThreadEx() {
		// TODO Auto-generated constructor stub
		thread=new Thread(task);
	}
	
	public void start()
	{
		this.thread.start();
	}
	
	public void stopRunning()
	{
		isRunning=false;
		thread.interrupt();
	}
	
	public boolean isRunning()
	{
		return this.isRunning;
	}
	
	
	// 接口函数
	protected abstract void runProcess();
	
	private Runnable task=new Runnable()
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			isRunning=true;
	        
			runProcess();
		}
	};
}
