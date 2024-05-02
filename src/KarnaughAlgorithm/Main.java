package KarnaughAlgorithm;



public class Main {

	public static void main(String[] args) {
		
		try {
			FileRead fileRead = new FileRead();
			fileRead.FileReader();

			Process process = new Process(fileRead.ReadedFileLineList.get(0));
			process.SetTruthTable();
			System.out.println("doğruluk tablosu:");
			process.PrintTruthTable();
			System.out.println("Fonksiyon İfadeleri:");
			process.PrintSumOfMinterms();
			process.PrintMultiplicationOfMaxterms();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
