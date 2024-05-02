package KarnaughAlgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileRead {

	ArrayList<String> ReadedFileLineList = new ArrayList<>();

	public void FileReader() {
		try {
			File file = new File("boole.txt");

			if (!file.exists()) {
				System.out.println("Dosya Bulunamadı");
			}

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String Line;

			while ((Line = bufferedReader.readLine()) != null) {
				this.ReadedFileLineList.add(Line);
			}

			bufferedReader.close();
			fileReader.close();

			System.out.println("boole.txt dosyası okundu.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
