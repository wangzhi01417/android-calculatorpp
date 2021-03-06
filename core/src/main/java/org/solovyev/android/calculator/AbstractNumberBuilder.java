/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package org.solovyev.android.calculator;

import jscl.NumeralBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.solovyev.android.calculator.math.MathType;
import org.solovyev.common.text.Strings;

/**
 * User: serso
 * Date: 12/15/11
 * Time: 9:01 PM
 */
public abstract class AbstractNumberBuilder {

	@Nonnull
	protected final CalculatorEngine engine;

	@Nullable
	protected StringBuilder numberBuilder = null;

	@Nullable
	protected NumeralBase nb;

	protected AbstractNumberBuilder(@Nonnull CalculatorEngine engine) {
		this.engine = engine;
		this.nb = engine.getNumeralBase();
	}

	/**
	 * Method determines if we can continue to process current number
	 *
	 * @param mathTypeResult current math type result
	 * @return true if we can continue of processing of current number, if false - new number should be constructed
	 */
	protected boolean canContinue(@Nonnull MathType.Result mathTypeResult) {
		boolean result = mathTypeResult.getMathType().getGroupType() == MathType.MathGroupType.number &&
				!spaceBefore(mathTypeResult) &&
				numeralBaseCheck(mathTypeResult) &&
				numeralBaseInTheStart(mathTypeResult.getMathType()) || isSignAfterE(mathTypeResult);
		return result;
	}

	private boolean spaceBefore(@Nonnull MathType.Result mathTypeResult) {
		return numberBuilder == null && Strings.isEmpty(mathTypeResult.getMatch().trim());
	}

	private boolean numeralBaseInTheStart(@Nonnull MathType mathType) {
		return mathType != MathType.numeral_base || numberBuilder == null;
	}

	private boolean numeralBaseCheck(@Nonnull MathType.Result mathType) {
		return mathType.getMathType() != MathType.digit || getNumeralBase().getAcceptableCharacters().contains(mathType.getMatch().charAt(0));
	}

	private boolean isSignAfterE(@Nonnull MathType.Result mathTypeResult) {
		if (!isHexMode()) {
			if ("-".equals(mathTypeResult.getMatch()) || "+".equals(mathTypeResult.getMatch())) {
				final StringBuilder localNb = numberBuilder;
				if (localNb != null && localNb.length() > 0) {
					if (localNb.charAt(localNb.length() - 1) == MathType.POWER_10) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isHexMode() {
		return nb == NumeralBase.hex || (nb == null && engine.getNumeralBase() == NumeralBase.hex);
	}

	@Nonnull
	protected NumeralBase getNumeralBase() {
		return nb == null ? engine.getNumeralBase() : nb;
	}
}
