package com.kyhsgeekcode.scala2java;

import android.util.*;
import java.io.*;
import java.lang.reflect.*;

public class Scala2Java
{
	static Method main=(Method) null;
	static MainActivity ac;
	public static void Run(MainActivity a)
	{
		Class clazz=com.github.scalatojava.Main.class;
		Method[] methods=clazz.getDeclaredMethods();
		ac=a;
		for(Method m:methods)
		{
			if("main".equals(m.getName()))
			{
				if(Modifier.isStatic(m.getModifiers()))
				{
					if(Modifier.isPublic(m.getModifiers()))
					{
						main=m;
						break;
					}
				}
			}
		}
		if(main==null)
		{
			a.print("No main");
			return;
		}
		String path="";
		File file = null;
		do
		{
			try
			{
				a.print("Enter a valid path");
				file = new File(a.readLine());
			}
			catch (InterruptedException e)
			{
				a.print(Log.getStackTraceString(e));
				ac=(MainActivity) null;
				return;
			}
		}while(file==null||file.exists());
		if(file.isDirectory())
		{
			Recursive(file);
		}else
		{
			OneFile(file);
		}
		ac=(MainActivity) null;
	}

	private static void OneFile(File file)
	{
		String outname=file.getAbsolutePath();
		//int index=outname.lastIndexOf(".scala");
		int l=outname.length();
		if(l>7)
		{
			if(outname.substring(l-6,l-1).equalsIgnoreCase(".scala"))
			{
				outname=outname.substring(0,l-7);
			}
		}
		outname=outname+".java";
		ac.print("Processing "+file.getAbsolutePath()+" to "+outname);
		File out=new File(outname);
		try
		{
			out.createNewFile();
			System.setIn(new FileInputStream(file));
			System.setOut(new PrintStream(out));
			main.invoke((Object)null, (Object)new String[]{});
		}
		catch (IllegalAccessException |IllegalArgumentException |InvocationTargetException|IOException e)
		{
			ac.print(Log.getStackTraceString(e));
		}
		return ;
	}

	private static void Recursive(File file)
	{
		if(!file.exists())
		{
			ac.print("ERROR:"+file.getName()+" does not exist");
			return;
		}
		if(file.isDirectory())
		{
			File[] files=file.listFiles();
			if(files==null)
			{
				return;
			}
			for(File f:files)
			{
				Recursive(f);
			}
		}else if(file.isFile())
		{
			OneFile(file);
		}
		return ;
	}
}
