import java.util.Iterator;
import data_structures.*;

public class AllTester {
	public static void main(String [] args){
		DictionaryADT<Integer,String> dictionary
			//= new BinarySearchTree<Integer,String>();
		//	=new HashTable<Integer,String>(30000);
		//	= new OrderedArrayDictionary<Integer,String>(15);
			 = new RedBlackTree<Integer,String>();
	
		
		
		//System.out.println(dictionary.delete(5));
		//System.out.println(dictionary.contains(40));
		
		System.out.println(dictionary.add(4,"banana"));
		System.out.println(dictionary.add(4,"pie"));
		
		
		
		System.out.println(dictionary.add(6,"persimum"));
		System.out.println(dictionary.add(6,"tomato"));
		System.out.println(dictionary.add(60,"blackberry"));
		System.out.println(dictionary.getKey("persimu"));
		System.out.println(dictionary.getValue(50));
		
		System.out.println(dictionary.add(44,"mango"));
		System.out.println(dictionary.contains(5));
		
		System.out.println(dictionary.getValue(44));
		
		Integer [] array ={50,60,85,92,87,49,48,43,44,40};//keys array
		System.out.println("Size = "+dictionary.size());
		
			for(int i:array){
				//System.out.println("removing "+i+", "+dictionary.getValue(i));
					//dictionary.delete(i);
		}
		
		//int size = 100;
	   // for(int i=0; i < size; i++)
	   // 	System.out.println(dictionary.add(i,"value"));    
		
		//for(int i=size-1; i >= 0; i--)
	   //        System.out.println(dictionary.delete(i));
		
		//System.out.println("contains 20 =" +dictionary.contains(20));//test contains()
		//System.out.println("getValue of key 4 =" +dictionary.getValue(20));//test getValue()
		int number = 50;
		String value = dictionary.getValue(number);
		
		//System.out.println("removal of "+ number+" " +value +" with parent "+dictionary.parentNode.getKey() +" = " +dictionary.delete(number));
		//System.out.println("simple delete of "+number+" = " + dictionary.delete(number));
		//System.out.println("value deleted element = "+dictionary.getValue(number));//find erased node
		//System.out.println("getKey("+value+") =" +dictionary.getKey(value));
  
		System.out.println("Size before iterators = "+dictionary.size());
				
		//for(DictionaryNode e: dictionary)
			//System.out.println(e );
		
		Iterator<Integer> keys = dictionary.keys();
		Iterator<String> values = dictionary.values();

		while(keys.hasNext()) {
            System.out.print(keys.next());
            System.out.print("   " + values.next());
            System.out.println();
       } 
		System.out.println("Size = "+dictionary.size());

	}
}