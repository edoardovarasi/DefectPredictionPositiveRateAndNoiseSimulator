/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package entity;

/**
 * 
 * Dimensions for a subdataset: size, number of negative and positive instances
 * 
 * @author Edoardo Varasi
 * @email edoardo.varasi@gmail.com
 *
 */
public class SubdatasetDimensions {
	
	// positive instances
	private int p;
	// negative instances
	private int n;
	
	public SubdatasetDimensions(int p,int n) {
	
		this.p=p;
		this.n=n;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	// size (total number of instances)
	public int getTotalNumberOfElements(){
		return p+n;
	}
	
	@Override
	public String toString() {
		return new String("#tot="+ this.getTotalNumberOfElements() +", #pos="+this.p+", #neg="+this.n);
	}

}

