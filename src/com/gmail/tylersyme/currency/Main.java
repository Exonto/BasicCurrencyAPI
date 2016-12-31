package com.gmail.tylersyme.currency;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Main
{
	
	class Vehicle 
	{
		public double weight = 6000.0;
	}
	class Car extends Vehicle 
	{
		public int year = 1990;
		public Car()
		{
			this.weight = 4000.0;
		}
	}
	class Chevy extends Car 
	{
		public String brand = "Chevy";
		public Chevy()
		{
			this.year = 2001;
			this.weight = 3000.0;
		}
	}

	public static void main(String[] args)
	{
		Stream<String> s = Stream.of("A", "BC", "DEF");
		//s.sorted(Comparator.reverseOrder()).forEach(System.out::println);
		
		s.map(String::toLowerCase).forEach(System.out::println);
	}
	
	public static String flip(String original)
	{
		String flipped = "";
		for (int x = original.length() - 1; x >= 0; x--)
		{
			flipped += original.charAt(x);
		}
		
		return flipped;
	}
	
	public static Optional<Double> getAverage(List<? extends Number> list)
	{
		Optional<Double> result;
		if (list.isEmpty() == false)
		{
			double sum = 0.0;
			for (Number num : list)
			{
				sum += num.doubleValue();
			}
			
			result = Optional.of(sum / list.size());
		} else {
			result = Optional.empty();
		}
		
		return result;
	}
	
	public static void showSize(List<?> list)
	{
		System.out.println(list.get(0));
	}
	
	public static void showVehicleWeight(List<? extends Vehicle> list)
	{
		System.out.println(list.get(0).weight);
	}
	
	public static void addVehicle(List<? super Chevy> list)
	{
		list.add(new Main().new Chevy());
	}

}















