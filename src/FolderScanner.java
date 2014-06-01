import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;


public class FolderScanner implements Runnable{
	//initializing Posting File structure
	List <PostingFileElement> postingFile = new ArrayList<>();
	List <IndexFileElement> indexFile = new ArrayList<>();

	@Override
	public void run() {
		while (true){
			File folder = new File(System.getProperty("user.dir")+"\\db");
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	
			    	// In case its the first file to be indexed
			    	if (postingFile.isEmpty()){
			    		postingFile.add(new PostingFileElement(file.getPath(), postingFile.size() ));
			    		System.out.println("added - " + postingFile.get(postingFile.size()-1).getM_path() + " and its Document number is: "+postingFile.get(postingFile.size()-1).getM_docNum());
			    		parseFile(file,postingFile.size()-1);
			    		
			    	// In case this is not the first file to be indexed
			    	}else{
			    		boolean exists = false;
			    		// check if we indexed this file path before
			    		for (int i=0; i<postingFile.size(); i++){
			    			// if file path exist
					    	if (postingFile.get(i).getM_path().equals(file.getPath()) ){
					    		exists=true;
					    		break;
					    	}
			    		}
			    		// if file does not exist -> Add file to list
			    		if (!exists){
			    			postingFile.add(new PostingFileElement(file.getPath(), postingFile.size() ));
				    		System.out.println("added - " + postingFile.get(postingFile.size()-1).getM_path() + " and its Document number is: "+postingFile.get(postingFile.size()-1).getM_docNum());
				    		parseFile(file, postingFile.size()-1);
			    		}

			    		// check if all path's are still valid
				    	for (int i=0; i<postingFile.size(); i++){
				    		File f = new File(postingFile.get(i).getM_path());
				    		// if not valid -> Remove file from list
				    		if (!f.exists()){
					    		System.out.println("added - " + postingFile.get(postingFile.size()-1).getM_path() + " and its Document number is: "+postingFile.get(postingFile.size()-1).getM_docNum());
				    			postingFile.remove(i);
				    		}			    		
				    	}
			    	}
			    }
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

/*This function does several things:
 * 1. Parses a text file
 * 2. Adds every word from the file to the Index file structure
 * 3. Sorts the Index file structure
 */
	private void parseFile(File file, int docNum) {
		String everything=null;
		String words[];
		try(BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Removing special characters from the text
		everything = everything.replaceAll("[!@#$%^&*\\[\\]\"()-=_+~:;<>?,.{}`|/]","");
		everything = everything.replaceAll("\r", "");
		everything = everything.replaceAll("\n", " ");
		words = everything.split(" ");
		for(String tmpWord : words){
			tmpWord.trim();
			if (tmpWord.equals("")){	//Makes sure we dont add empty words to the index file
				continue;
			}
			else{
				indexFile.add(new IndexFileElement(tmpWord.toLowerCase(), docNum));
			}
		}
		//Sorting the index file by an alfabetic order
		Collections.sort(indexFile);
		//Removing duplicates from the index file
		indexFile = removeDuplicates(indexFile);
		System.out.println(indexFile.toString());
	}

	private List<IndexFileElement> removeDuplicates(List<IndexFileElement> indexFile) {
		List<IndexFileElement> tmpIndexFile = new ArrayList<IndexFileElement>();
		boolean added = false;
		for (int i=0; i<indexFile.size(); i++){
			if (tmpIndexFile.isEmpty()==false){
				for (int j=0; j<tmpIndexFile.size() ; j++){
					added=false;
						if ((indexFile.get(i).getM_word().equals(tmpIndexFile.get(j).getM_word())) && (indexFile.get(i).getM_docNum() == tmpIndexFile.get(j).getM_docNum())){
							tmpIndexFile.get(j).setM_frequency(tmpIndexFile.get(j).getM_frequency() + indexFile.get(i).getM_frequency());
							added=true;
							break;
						}
					}
				if (added==false){
					tmpIndexFile.add(new IndexFileElement(indexFile.get(i).getM_word(), indexFile.get(i).getM_docNum() ,indexFile.get(i).getM_frequency() ));
					

				}
			//inserting first element into tmpIndexFile
			}else{
				tmpIndexFile.add(new IndexFileElement(indexFile.get(i).getM_word(), indexFile.get(i).getM_docNum() ,indexFile.get(i).getM_frequency() ));
			}
		}
	
		return tmpIndexFile;
	}
}

	

