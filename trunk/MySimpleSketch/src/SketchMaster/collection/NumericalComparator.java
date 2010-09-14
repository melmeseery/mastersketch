//// See bottom of source code for software license.

package SketchMaster.collection;

import java.io.Serializable;
import java.util.*;

/**
 * Compare Number objects.
 * 
 * <P>
 * This software is distributed under the <A
 * HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt"> Berkeley Software
 * License</A>.
 * 
 * <PRE>
 * Revisions:  - GUIRLib-v1.0-1.0.0, Jun 16 1999, JH
 *               Created class
 *             - GUIRLib-v1.2-1.0.0, Jun 22 2000, JH
 *               Touched for GUIRLib release
 *             - GUIRLib-v1.3-1.0.0, Aug 11 2000, JH
 *               Touched for GUIRLib release
 *             - GUIRLib-v1.4-1.0.0, Aug 31 2000, JH
 *               Touched for GUIRLib release
 * </PRE>
 * 
 * @author (<A HREF="mailto:jasonh@cs.berkeley.edu">jasonh@cs.berkeley.edu</A>)
 * @since JDK 1.2
 * @version GUIRLib-v1.4-1.0.0, Aug 31 2000
 */
public class NumericalComparator implements Comparator, Serializable {

	// ===========================================================================
	// === DEFAULTS AND CUSTOMIZATION ========================================

	/**
	 * 
	 */
	private static final long serialVersionUID = 2452111586088162364L;
	private boolean flagUsePerturbations = false;

	// -----------------------------------------------------------------

	/**
	 * Add perturbations to the values or not? Basically, if set to true, we
	 * will add two different small values to each of the numbers before
	 * comparison, to avoid equal values.
	 * 
	 * @param flag
	 *            is true if perturbations are to be used, false otherwise.
	 */
	public void setUsePerturbations(boolean flag) {
		flagUsePerturbations = flag;
	} // of setUsePerturbations

	// === DEFAULTS AND CUSTOMIZATION ========================================
	// ===========================================================================

	// ===========================================================================
	// === COMPARISON ========================================================

	public int compare(Object obj1, Object obj2) {
		if (obj1 instanceof Number && obj2 instanceof Number) {
			// // 1. Get numerical values.
			int returnVal = 0;
			double val1 = ((Number) obj1).doubleValue();
			double val2 = ((Number) obj2).doubleValue();

			// // 2. Do comparisons.
			if (val1 < val2) {
				returnVal = -1;
				
			
			} else {
				if (val1 > val2) {
					returnVal = 1;
				} else {
					returnVal = 0;
				}
			}

			// // 3. I actually thought about using perturbations, but this
			// // seems much simpler.
			if (flagUsePerturbations == true) {
				returnVal = 1;
			}


//			logger.info(" the values are   "+val1+  "    <   "+returnVal+"    "+val2+" ("
//					+ this.getClass().getSimpleName() + "    "
//					+ (new Throwable()).getStackTrace()[0].getLineNumber()
//					+ "  )  ");
			return (returnVal);
		}
		throw new ClassCastException("Only know how to handle numbers.");
	} // of compare

	// === COMPARISON ========================================================
	// ===========================================================================

} // of class

// ==============================================================================

/*
 * Copyright (c) 2000 Regents of the University of California. All rights
 * reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. All advertising materials mentioning features or use of this software must
 * display the following acknowledgement:
 * 
 * This product includes software developed by the Group for User Interface
 * Research at the University of California at Berkeley.
 * 
 * 4. The name of the University may not be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

