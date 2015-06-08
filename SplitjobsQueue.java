import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.ArrayUtils;
import org.gridgain.grid.*;
import org.gridgain.grid.events.GridEventAdapter;
import org.gridgain.grid.logger.GridLogger;
import org.gridgain.grid.resources.GridLoadBalancerResource;
import org.gridgain.grid.resources.GridLoggerResource;
import org.gridgain.grid.spi.communication.tcp.GridTcpCommunicationSpi;
import org.gridgain.grid.typedef.G;
import org.gridgain.grid.typedef.X;
import org.jetbrains.annotations.Nullable;

public class SplitjobsQueue {

	    @GridLoggerResource
	    private static char []alphanums={'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','0'};
	    public static void main(String[] args) throws GridException {
	    Scanner cin=new Scanner(System.in);
	    System.out.print("Enter The password to be cracked: ");
	    final String passWord=cin.next();
	   // final String toCompare=cin.next();
	    System.out.print("Enter the Max characters for the password: ");
	    final int max=cin.nextInt();
	    	if (args.length == 0)
	            G.start();
	        else
	            G.start(args[0]);

	        GridTask<String, String> task = new GridTaskSplitAdapter<String, String>() {
	    @Override
	    public Collection<? extends GridJob> split(int gridSize, String phrase) throws GridException {
	        String[] words = new PasswordCrack().splitArray(gridSize);
            
	        List<GridJob> jobs = new ArrayList<GridJob>(alphanums.length);
           
	        for (final char word : alphanums) {
	            jobs.add(new GridJobAdapterEx()  {
	                public Serializable execute() {
	                    
	                	String abcd=new String("");
                        try {
							abcd= bruteForce(new Character(word).toString(),passWord,max);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                     	 
                        
                    		                    		 
	                    return abcd;
	                }
	                
	            });
	        }

	        return jobs;
	    }
	    @Override public GridJobResultPolicy result(GridJobResult res, List<GridJobResult> rcvd) {
	    	//X.println("sfgs");
            return GridJobResultPolicy.REDUCE;
        }
	    public String reduce(List<GridJobResult> results) throws GridException {
	        String agg = new String("");
	        for (GridJobResult res : results) {
	            String add = res.getData();

	            agg+=add;
	        }

	        return agg;
	    }
	};
	try {
        GridTaskFuture<String> fut = G.grid().execute(task,null);
        
        String phraseLen = fut.get();

        X.println(">>>");
        X.println(">>> Found Password!'" + phraseLen + "'.");
        X.println(">>>");
    }
    finally {
        G.stop(true);
    }
		}
	    public static String bruteForce(String klo,String passw,int number) throws IOException
		{
	    	
			String result=new String(""); 
			char loarr[]={'a','b','c','d','e','f','g','h','j','i','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
			char uparr[]={'A','B','C','D','E','F','G','H','J','I','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
			char nuarr[]={'1','2','3','4','5','6','7','8','9','0'};
			char toarr[]=ArrayUtils.addAll(ArrayUtils.addAll(loarr, uparr),nuarr);
//			File fw=new File("fsdf.txt");
//			PrintWriter pw=new PrintWriter(fw);
			Character get[]=new Character[toarr.length];
//			char[] get=klo.toCharArray();
			System.out.println("klo::  "+klo);
		//	System.in.read();
		//	System.in.read();
			for(int i=0;i<toarr.length;i++)
			{
				get[i]=toarr[i];
			}
			List<StringBuilder> abcd=new CopyOnWriteArrayList<StringBuilder>();

			/*for(int i=0;i<get.length;i++)
			{
				abcd.add(new StringBuilder(Character.toString(get[i])));
			}*/
			char ch[]=klo.toCharArray();
//			System.out.println(ch);
			for(int i=0;i<ch.length;i++)
			{
//				System.out.println(abcd.toString()+":  "+ch[i]);
			abcd.add(new StringBuilder(Character.toString(ch[i])));
//			System.out.println(abcd.toString()+":  "+ch[i]);
			}
//			int number=4;
			System.out.println("abcd---->"+abcd.toString());
		    long size=0;
			total2:	for(int j=0;j<number;j++)
				{			
//				System.out.println("dsg");
//				System.out.println(abcd.toString());
					List<StringBuilder> dup=abcd.subList((int)size, abcd.size());
					size=abcd.size();
					ListIterator<StringBuilder> dupli=dup.listIterator();
			total1:		while(dupli.hasNext()) 
					{
//				System.out.println("dsg1");
						StringBuilder sb=(StringBuilder) dupli.next();
						int count=0;
						
						total:	for(int k=0;k<get.length;k++)
						{
//							System.out.println("dsg2");
							StringBuilder sb1=new StringBuilder(sb.toString()); 
						if(count==0)
						{
//							System.out.println("dsg1");
							sb=sb1.append(get[k]);
//							System.out.println("dsg5");
//							pw.println(sb.toString());
							abcd.add(sb);
//							System.out.println("dsg4");
							count++;
							
							
						}
						else
						{
						sb1.setCharAt(sb1.length()-1, get[k]);
//						pw.println(sb1.toString()+" :"+j);
						abcd.add(sb1);
						}
						if(sb1.toString().equals(passw))
						{
//							System.out.println("dsg7");
							result=sb1.toString();
							
							
							break total2;
						}
						System.out.println(sb1);
						 if(Thread.currentThread().isInterrupted()){
//		                        System.out.println("canceled");
		                        break total2;
						 }
						if(sb1.length()>number)
						{
//							System.out.println("dsg1: "+sb1.length()+": "+number);
							break total2;
						}
						else
							continue;
					}
					
				}
					
			}
			if(result!="")
			X.println(">>>> Found Password: "+result);
//		   gea.message(result);
//		   X.println(">>>>sent");
			return result;


			
		}
		
	    }