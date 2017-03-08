package needleman;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {

		/*
		 * blosmAminoInfo is an array which contains Amino Acid Information which is the first row and first column
		 * in a file blosm50.txt
		 * 
		 * blosm50Array is a two dimensional array which holds the value of blosm substitution matrix
		 */
		char[] blosmAminoInfo = new char[24];
		Integer[][] blosm50Array = new Integer[24][24];
		
		Scanner scanner = new Scanner(new InputStreamReader(System.in));  // scanner to read input from console
		System.out.print("provide Blosm50 filename with directory" + '\n');
		System.out.flush();
		String blosmFilename = scanner.nextLine(); // To read input file name
													// from
													// the console
		System.out
				.print("provide Input Sequence filename with directory" + '\n');
		

		String sequenceFile = scanner.nextLine();
		
		// blosm and sequence file as well as buffered reader and writer initialization
		
		File ipFile = new File(blosmFilename);
		File seqIpFile = new File(sequenceFile);

		BufferedReader blosmReader = BufferReaderAndWriter.getReader(ipFile);
		BufferedReader seqReader = BufferReaderAndWriter.getReader(seqIpFile);
		
		/*
		 * Parse blosm50 file and load it in memory (array)
		 */
		
		Sequence seq1 = null;
		Sequence seq2 = null;
		String blosmLine;
		String seqLine;
		int count = 0;

		while ((blosmLine = blosmReader.readLine()) != null) {
			count++;

			if (count == 1) {

				String[] localArr = blosmLine.trim().split("\\s+");

				for (int i = 0; i < localArr.length; i++) {

					blosmAminoInfo[i] = localArr[i].trim().charAt(0);

				}

			} else {

				String[] localArr = blosmLine.trim().split("\\s+");

				for (int i = 1; i < localArr.length; i++) {

					blosm50Array[count - 2][i - 1] = Integer
							.parseInt(localArr[i].trim());

				}

			}

		}
		
		
		/*
		 * Read two Sequence Input from file and store the information in two different Sequence object 
		 */

		if ((seqLine = seqReader.readLine()) != null) { 
			
			seq1 = new Sequence();
			String protSeq;
			StringBuilder value = new StringBuilder();
			if (seqLine.startsWith(">")) {

				seq1.setSeqID(seqLine);
				while ((protSeq = seqReader.readLine()) != null
						&& protSeq.length() > 0) {

					if (protSeq.charAt(0) != '>') {
						value.append(protSeq);

					}

				}
				seq1.setSeq(value.toString());

			}

		}

		if ((seqLine = seqReader.readLine()) != null) { 
			
			seq2 = new Sequence();
			String protSeq;
			StringBuilder value = new StringBuilder();
			if (seqLine.startsWith(">")) {

				seq2.setSeqID(seqLine);
				while ((protSeq = seqReader.readLine()) != null
						&& protSeq.length() > 0) {

					if (protSeq.charAt(0) != '>') {
						value.append(protSeq);

					}

				}
				seq2.setSeq(value.toString());

			}

		}
		
		/*
		 * calcNeedleManMatrixAndSeqAllign() method calculates NeedleMan Score Matrix and pointer Matrix
		 * 									 and calculates the allignment
		 * param1: blosm50Array -- array which contains blosm50 substitution matrix
		 * param2: blosmAminoInfo -- contains the residue information of blosm matrix
		 * param3: smaller sequence length sequence object
		 * param4: bigger sequence length sequence object 
		 * 
		 */
		
		NeedleManImplementor nmi = new NeedleManImplementor();
		
		if(seq1.getSeq().length() > seq2.getSeq().length()){
			
			nmi.calcNeedleManMatrixAndSeqAllign(blosm50Array, blosmAminoInfo, seq2, seq1);
			
		}else{
			
			nmi.calcNeedleManMatrixAndSeqAllign(blosm50Array, blosmAminoInfo, seq1, seq2);
			
			
		}
		
		
		
	}

}
