package agent.elena;

import java.lang.instrument.Instrumentation;

public class AgentMemory {
	
	private static Instrumentation instrumentation;
	
	public static void premain(String args, Instrumentation instrumentation) {
		AgentMemory.instrumentation = instrumentation;
	}
	
	public static long getSize(Object obj) {
		if (instrumentation == null) {
			throw new IllegalStateException("Agent not initialised");
		}
		return instrumentation.getObjectSize(obj);
	}
}