package needleman;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class NeedleManImplementor {

	public void calcNeedleManMatrixAndSeqAllign(Integer[][] blosm50Array,
			char[] blosmAminoInfo, Sequence seq1, Sequence seq2)
			throws IOException {

		// up = 1
		// left = -1
		// diagonal = 0;
		// default = -2
		/*
		 * Initialize NeedleMan Matrix and Pointer Matrix
		 * and both sequence char array
		 * assign d = 8 and first element of pointer matrix ---  pointerMatrix[0][0] = -2 (default value)
		 */
		
		int seq1Len = seq1.getSeq().length();
		int seq2Len = seq2.getSeq().length();
		Integer[][] needleManMatrix = new Integer[seq2Len + 1][seq1Len + 1];
		Integer[][] pointerMatrix = new Integer[seq2Len + 1][seq1Len + 1];
		char[] seq1Chars = seq1.getSeq().toCharArray();
		char[] seq2Chars = seq2.getSeq().toCharArray();
		int d = 8;
		pointerMatrix[0][0] = -2;
		
		/*
		 * calculate value for first row and first column and assign it to needleman and pointer matrix
		 * 
		 */

		for (int x = 0; x < seq1Len + 1; x++) {

			needleManMatrix[0][x] = -x * d;
			if (x > 0) {

				pointerMatrix[0][x] = -1;

			}

		}

		for (int y = 0; y < seq2Len + 1; y++) {

			needleManMatrix[y][0] = -y * d;

			if (y > 0) {

				pointerMatrix[y][0] = 1;

			}

		}

		/*
		 * code below calculates the value for each element of Needleman matrix, assign value to it then,
		 * assign pointer information for each corresponding element to pointer matrix
		 * 
		 * here i represents col
		 * here j represents row
		 */
		

		for (int j = 1; j <= seq2Len; j++) {

			for (int i = 1; i <= seq1Len; i++) {

				char seq1Char = seq1Chars[i - 1];
				char seq2Char = seq2Chars[j - 1];
				int seq1CharIndex = -1;
				int seq2CharIndex = -1;

				for (int k = 0; k < blosmAminoInfo.length; k++) {

					if (blosmAminoInfo[k] == seq1Char) {

						seq1CharIndex = k;
					}

					if (blosmAminoInfo[k] == seq2Char) {

						seq2CharIndex = k;

					}

				}

				// here Math.max() will return same number if two arguments to the function is same.
				// thus here if all the three values are same then maxvalue
				// will be val1 (diagonal element)

				int val1 = needleManMatrix[j - 1][i - 1]
						+ blosm50Array[seq2CharIndex][seq1CharIndex];
				int val2 = needleManMatrix[j - 1][i] - d;
				int val3 = needleManMatrix[j][i - 1] - d;
				int maxValue = Math.max(Math.max(val1, val2), val3);
				needleManMatrix[j][i] = maxValue;

				// here j-1, i-1 means diagonal (0) --------- j-1, i is UP (1),
				// and j, i-1, is left (-1)

				if (maxValue == val1) {

					pointerMatrix[j][i] = 0;

				} else if (maxValue == val2) {

					pointerMatrix[j][i] = 1;

				} else {

					pointerMatrix[j][i] = -1;

				}

				
			}

		}

		/*
		 * This block prints NeedleManMatrix and the Pointer matrix in a file
		 */
		
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
		System.out.println("Provide the output file to save NeedleMan Matrix");
		String NeedleManOPString = scanner.nextLine();
		System.out.println("Provide the output file to save Pointer Matrix");
		String pointerMatrixString = scanner.nextLine();
		System.out.println("Provide the output file to save Alligned Sequence");
		String allignedSequence = scanner.nextLine();
	
	
		File needleManWriterFile = new File(NeedleManOPString);
		File pointerMatrixWriterFile = new File(pointerMatrixString);
		File allignedSequenceFile = new File(allignedSequence);
		BufferedWriter needleManWriter = BufferReaderAndWriter
				.getWriter(needleManWriterFile);
		BufferedWriter pointerMatrixWriter = BufferReaderAndWriter
				.getWriter(pointerMatrixWriterFile);
		BufferedWriter allignedSequenceWriter = BufferReaderAndWriter
				.getWriter(allignedSequenceFile);

		for (int l = 0; l <= seq2Len; l++) {

			for (int m = 0; m <= seq1Len; m++) {

				needleManWriter.write(needleManMatrix[l][m] + ",");

			}

			needleManWriter.write("\n");
		}

		for (int l = 0; l <= seq2Len; l++) {

			for (int m = 0; m <= seq1Len; m++) {

				pointerMatrixWriter.write(pointerMatrix[l][m] + ",");

			}

			pointerMatrixWriter.write("\n");
		}

		needleManWriter.close();
		pointerMatrixWriter.close();

		/*
		 * block below back tracks the Needle Man matrix on the basis of pointer
		 * matrix and writes the sequence alignment
		 */
		
		ArrayList<Character> seq1Alligned = new ArrayList<Character>();
		ArrayList<Character> seq2Alligned = new ArrayList<Character>();

		int n = seq2Len;
		int o = seq1Len;
		int loopIndex = seq2Len;

		while (loopIndex >= 1) {

			int pointerElement = pointerMatrix[n][o];
			if (pointerElement == 0) {

				seq1Alligned.add(seq1Chars[o - 1]);
				seq2Alligned.add(seq2Chars[n - 1]);
				o--;
				n--;
				loopIndex--;

			} else if (pointerElement == 1) {

				seq1Alligned.add('-');
				seq2Alligned.add(seq2Chars[n - 1]);

				n--;
				loopIndex--;

			} else if (pointerElement == -1) {

				seq1Alligned.add(seq1Chars[o - 1]);
				seq2Alligned.add('-');

				o--;

			}

		}
		
		// Write Alligned Sequence in a file

		for (int alpha = seq1Alligned.size() - 1; alpha >= 0; alpha--) {

			System.out.print(seq1Alligned.get(alpha));
			allignedSequenceWriter.write(seq1Alligned.get(alpha));

		}

		System.out.print("\n");
		allignedSequenceWriter.newLine();
		for (int beta = seq2Alligned.size() - 1; beta >= 0; beta--) {

			System.out.print(seq2Alligned.get(beta));
			allignedSequenceWriter.write(seq2Alligned.get(beta));

		}

		allignedSequenceWriter.close();

	}
}
