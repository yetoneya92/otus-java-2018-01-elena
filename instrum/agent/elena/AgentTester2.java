package agent.elena;

public class AgentTester2 {
	public static void main(String[] args) {
                String str="";
                System.out.print("str=\"\": ");
		printObjectSize(str);

                str=new String("");
                System.out.print("str=new String(\"\"): ");
                printObjectSize(str);
           
                str="abc";
                System.out.print("str=\"abc\": ");
                printObjectSize(str);

                str=new String("abc");
                System.out.print("str=new String(\"abc\"): ");
                printObjectSize(str);
              
                String[]strs=new String[10];
                System.out.print("strs=new String[10]: ");
                Long m=AgentMemory.getSize(strs);
                printObjectSize(strs);
 
                strs=new String[100];
                System.out.print("strs=new String[100]: ");
                Long n=AgentMemory.getSize(strs);
                printObjectSize(strs);

                long len=(n-m)/90;
                System.out.println("String size in array: "+len);
                long title=(m*10-n)/9;
                System.out.println("Title size in array: "+title);

                strs[0]="abc";
                System.out.print("strs[0]=\"abc\": ");
                printObjectSize(strs);

                strs[1]="abc";
                System.out.print("strs[1]=\"abc\": ");
                printObjectSize(strs);
                
}

	public static void printObjectSize(Object obj) {
		System.out.println(String.format("%s, size=%s", obj.getClass()
				.getSimpleName(), AgentMemory.getSize(obj)));
	}
}

