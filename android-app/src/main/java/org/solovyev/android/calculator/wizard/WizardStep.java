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

package org.solovyev.android.calculator.wizard;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import org.solovyev.android.App;
import org.solovyev.android.Views;
import org.solovyev.android.calculator.CalculatorApplication;
import org.solovyev.android.calculator.CalculatorPreferences;
import org.solovyev.android.calculator.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.solovyev.android.calculator.wizard.ChooseLayoutWizardStep.LAYOUT;
import static org.solovyev.android.calculator.wizard.ChooseModeWizardStep.MODE;
import static org.solovyev.android.calculator.wizard.OnScreenCalculatorWizardStep.ONSCREEN_CALCULATOR_ENABLED;

/**
 * User: serso
 * Date: 6/16/13
 * Time: 9:17 PM
 */
enum WizardStep {

	welcome(WelcomeWizardStep.class, R.string.cpp_wizard_welcome_title, R.string.cpp_wizard_start),

	choose_layout(ChooseLayoutWizardStep.class, R.string.cpp_wizard_layout_title) {
		@Override
		boolean onNext(@Nonnull Fragment f) {
			final ChooseLayoutWizardStep fragment = (ChooseLayoutWizardStep) f;

			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(f.getActivity());

			final CalculatorLayout layout = fragment.getSelectedLayout();
			layout.apply(preferences);

			return true;
		}

		@Nullable
		@Override
		Bundle getFragmentArgs() {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalculatorApplication.getInstance());

			final Bundle bundle = new Bundle();
			bundle.putSerializable(LAYOUT, CalculatorLayout.fromGuiLayout(CalculatorPreferences.Gui.layout.getPreference(preferences)));
			return bundle;
		}

		@Override
		public boolean isVisible() {
			return Views.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE, App.getApplication().getResources().getConfiguration());
		}
	},

	choose_mode(ChooseModeWizardStep.class, R.string.cpp_wizard_mode_title) {
		@Override
		boolean onNext(@Nonnull Fragment f) {
			final ChooseModeWizardStep fragment = (ChooseModeWizardStep) f;

			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(f.getActivity());

			final CalculatorMode mode = fragment.getSelectedMode();
			mode.apply(preferences);

			return true;
		}

		@Nullable
		@Override
		Bundle getFragmentArgs() {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalculatorApplication.getInstance());

			final Bundle bundle = new Bundle();
			bundle.putSerializable(MODE, CalculatorMode.fromGuiLayout(CalculatorPreferences.Gui.layout.getPreference(preferences)));
			return bundle;
		}
	},

	on_screen_calculator(OnScreenCalculatorWizardStep.class, R.string.cpp_wizard_onscreen_calculator_title) {
		@Override
		boolean onNext(@Nonnull Fragment f) {
			final OnScreenCalculatorWizardStep fragment = (OnScreenCalculatorWizardStep) f;

			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(f.getActivity());

			CalculatorPreferences.OnscreenCalculator.showAppIcon.putPreference(preferences, fragment.isOnscreenCalculatorEnabled());

			return true;
		}

		@Nullable
		@Override
		Bundle getFragmentArgs() {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalculatorApplication.getInstance());

			final Bundle bundle = new Bundle();
			bundle.putSerializable(ONSCREEN_CALCULATOR_ENABLED, CalculatorPreferences.OnscreenCalculator.showAppIcon.getPreference(preferences));
			return bundle;
		}
	},

	drag_button(DragButtonWizardStep.class, R.string.cpp_wizard_dragbutton_title),

	last(FinalWizardStep.class, R.string.cpp_wizard_final_title);

	@Nonnull
	private final Class<? extends Fragment> fragmentClass;

	private final int titleResId;
	private final int nextButtonTitleResId;



	WizardStep(@Nonnull Class<? extends Fragment> fragmentClass, int titleResId) {
		this(fragmentClass, titleResId, R.string.cpp_wizard_next);
	}
	WizardStep(@Nonnull Class<? extends Fragment> fragmentClass, int titleResId, int nextButtonTitleResId) {
		this.fragmentClass = fragmentClass;
		this.titleResId = titleResId;
		this.nextButtonTitleResId = nextButtonTitleResId;
	}

	public String getFragmentTag() {
		return name();
	}

	@Nonnull
	Class<? extends Fragment> getFragmentClass() {
		return fragmentClass;
	}

	int getTitleResId() {
		return titleResId;
	}

	int getNextButtonTitleResId() {
		return nextButtonTitleResId;
	}

	boolean onNext(@Nonnull Fragment fragment) {
		return true;
	}

	boolean onPrev(@Nonnull Fragment fragment) {
		return true;
	}

	@Nullable
	Bundle getFragmentArgs() {
		return null;
	}

	public boolean isVisible() {
		return true;
	}

	@Nonnull
	public String getName() {
		return name();
	}
}
