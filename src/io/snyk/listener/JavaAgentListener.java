package io.snyk.listener;

import java.lang.management.ManagementFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sun.tools.attach.VirtualMachine;

public class JavaAgentListener implements ServletContextListener {

	private static String pid = "-1";
	
//	   static
//	    {
//	            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
//	            unsafeConstructor.setAccessible(true);
//	            unsafe = unsafeConstructor.newInstance();
//	            constructorModifiers = Constructor.class.getDeclaredField("modifiers");
//	            constructorModifiersOffset = unsafe.objectFieldOffset(constructorModifiers);
//	            methodModifiers = Method.class.getDeclaredField("modifiers");
//	            methodModifiersOffset = unsafe.objectFieldOffset(methodModifiers);
//	            fieldModifiers = Field.class.getDeclaredField("modifiers");
//	            fieldModifiersOffset = unsafe.objectFieldOffset(fieldModifiers);
//	            setAccessible = AccessibleObject.class.getDeclaredMethod("setAccessible0", boolean.class);
//	            setForceAccessible(setAccessible);
//	    }
//	    private static boolean setForceAccessible(AccessibleObject accessibleObject)
//	    {
//	        try
//	        {
//	            if (accessibleObject instanceof Constructor)
//	            {
//	                Constructor<?> object = (Constructor<?>) accessibleObject;
//	                unsafe.getAndSetInt(object, constructorModifiersOffset, addPublicModifier(object.getModifiers()));
//	                return true;
//	            }
//	            if (accessibleObject instanceof Method)
//	            {
//	                Method object = (Method) accessibleObject;
//	                unsafe.getAndSetInt(object, methodModifiersOffset, addPublicModifier(object.getModifiers()));
//	                return true;
//	            }
//	            if (accessibleObject instanceof Field)
//	            {
//	                Field object = (Field) accessibleObject;
//	                unsafe.getAndSetInt(object, fieldModifiersOffset, addPublicModifier(object.getModifiers()));
//	                return true;
//	            }
//	            return false;
//	        }
//	        catch (Exception e)
//	        {
//	            e.printStackTrace();
//	            return false;
//	        }
//	    }
//
//	    private static int addPublicModifier(int mod)
//	    {
//	        mod &= ~ (Modifier.PRIVATE);
//	        mod &= ~ (Modifier.PROTECTED);
//	        mod |= (Modifier.PUBLIC);
//	        return mod;
//	    }
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			pid = ManagementFactory.getRuntimeMXBean().getName();
			pid = (pid.indexOf('@')<0)?pid:pid.substring(0,pid.indexOf('@'));
			VirtualMachine jvm = VirtualMachine.attach(pid);
			String agentPath = sce.getServletContext().getRealPath("/WEB-INF/lib/snyk-java-runtime-agent.jar");
			jvm.loadAgent(agentPath);
			jvm.detach();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// do nothing
	}

}
