/**
 * 
 */
package de.genialitaet.visualc;

/**
 * Emulates the pseudo-random-number-generator of visual c++
 * http://www.codeproject.com/useritems/PRNG.asp
 * states it is calculated as
 * return (((holdrand = holdrand * 214013L + 2531011L) >> 16) & 0x7fff); 
 * @author Franky
 *
 */
public class Random {
	private int holdrand;
	
	public Random() {
		holdrand = 1;
	}
	
	public Random(int seed) {
		holdrand = seed;
	}
	
	public int rand() {
		return (((holdrand = holdrand * 214013 + 2531011) >> 16) & 0x7fff);
	}
	
	public void setSeed(int value) {
		holdrand = value;
	}
}
