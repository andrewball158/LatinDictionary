

import data_structures.*;
import java.util.Iterator;

public class LatinDictionary {
    private DictionaryADT<String,String> dictionary;
    
    public LatinDictionary() {
    	dictionary = new HashTable<String,String>(30000); 
    	//dictionary = new BinarySearchTree<String,String>();
    	//dictionary = new RedBlackTree<String,String>();
    	//dictionary = new OrderedArrayDictionary<String,String>(); 
        }

    public void loadDictionary(String fileName) {
    	DictionaryEntry [] entries =  DictionaryReader.getDictionaryArray(fileName);
    	for (DictionaryEntry e: entries)
    		dictionary.add(e.getKey(),e.getValue());	
    }

    public boolean insertWord(String word, String definition) {
		return dictionary.add(word, definition);
        }

    public boolean deleteWord(String word) {
		return dictionary.delete(word);
        }

    public String getDefinition(String word) {
		return dictionary.getValue(word);
        }

    public boolean containsWord(String word) {
		return dictionary.contains(word);
        }
       
    public String[] getRange(String start, String finish) {
    	if (dictionary.size() == 0)
    		return null;
		String[] array = new String[dictionary.size()]; 
		Iterator<String> iter = dictionary.keys();
		int counter = 0;
		while (iter.hasNext()) {
			String tmp = iter.next();
			if (start.toLowerCase().compareTo(tmp) <= 0 && finish.toLowerCase().compareTo(tmp) >= 0) {
				array[counter] = tmp;
				counter++;
			}	
		}
		if (counter == 0)
			return null;
		String[] temp = new String[counter];
		for (int i = 0; i < counter; i++)
			temp[i] = array[i];
		array = temp;
		return array;
        }
            
    public Iterator<String> words() {
		return dictionary.keys();
        }

    public Iterator<String> definitions() {
		return dictionary.values();
        }
}   
