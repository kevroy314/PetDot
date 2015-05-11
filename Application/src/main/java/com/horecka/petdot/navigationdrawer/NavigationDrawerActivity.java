/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.horecka.petdot.navigationdrawer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import java.util.regex.Pattern;
import android.widget.TextView.OnEditorActionListener;
import android.app.DialogFragment;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class NavigationDrawerActivity extends Activity implements PreferencesAdapter.OnItemClickListener {
    public static final String PREFS_NAME = "PetDotPreferences";

    private DrawerLayout mDrawerLayout;
    private RecyclerView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPreferencesTitles;
    private String[] mPreferencesText;
    private String[] mPreferencesKeys;
    private String[] mPreferencesDefaultValues;

    private static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        prefs = getSharedPreferences(PREFS_NAME, 0);

        mTitle = mDrawerTitle = getTitle();

        mPreferencesTitles = getResources().getStringArray(R.array.preferences_titles);
        mPreferencesText = getResources().getStringArray(R.array.preferences_text);
        mPreferencesKeys = getResources().getStringArray(R.array.preferences_keys);
        mPreferencesDefaultValues = getResources().getStringArray(R.array.preferences_default_values);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // improve performance by indicating the list if fixed size.
        mDrawerList.setHasFixedSize(true);
        mDrawerList.setLayoutManager(new LinearLayoutManager(this));

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new PreferencesAdapter(mPreferencesTitles, this));
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listener for RecyclerView in the navigation drawer */
    @Override
    public void onClick(View view, int position) {
        selectItem(position);
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        FragmentManager fm = getFragmentManager();
        switch(position){
            case 0: //Home
                setTitle(mPreferencesTitles[position]);
                break;
            case 1: //IP Address
                EditTextDialog editTextDialog = EditTextDialog.newInstance(
                        mPreferencesText[position],
                        mPreferencesKeys[position], getResources().getString(R.string.ip_regex),
                        mPreferencesDefaultValues[position]);
                editTextDialog.show(fm, "dlg_edit_text");
                break;
            case 2: //Port
                EditTextDialog editPortDialog = EditTextDialog.newInstance(
                        mPreferencesText[position],
                        mPreferencesKeys[position], getResources().getString(R.string.port_regex),
                        mPreferencesDefaultValues[position]);
                editPortDialog.show(fm, "dlg_edit_text");
                break;
            case 3: //Connect
                ConnectButtonFragment connectFragment = ConnectButtonFragment.newInstance(
                        mPreferencesText[position]);
                connectFragment.show(fm, "dlg_connect_fragment");
                break;
            case 4: //Move Limits
                BoundsFragment boundsFragment = BoundsFragment.newInstance(
                        mPreferencesText[position],
                        mPreferencesKeys[position],
                        mPreferencesDefaultValues[position]);
                boundsFragment.show(fm, "dlg_edit_bounds");
                break;
            case 5: //Invert/Swap Axis
                InvertSwapFragment invertSwapFragment = InvertSwapFragment.newInstance(
                        mPreferencesText[position],
                        mPreferencesKeys[position],
                        mPreferencesDefaultValues[position]);
                invertSwapFragment.show(fm, "dlg_edit_invertswap");
                break;
            case 6: //Control Mode
                SpinnerDialog editControlMode = SpinnerDialog.newInstance(
                        mPreferencesText[position],
                        mPreferencesKeys[position],
                        getResources().getStringArray(R.array.control_mode_options),
                        Integer.parseInt(mPreferencesDefaultValues[position]));
                editControlMode.show(fm, "dlg_edit_dropdown");
                break;
            default:
                setTitle(mPreferencesTitles[position]);
                break;
        }


        // close the drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static void Save(String tag, String value, String defaultValue){
        SharedPreferences.Editor editor = prefs.edit();
        if(value == "")
            editor.putString(tag, defaultValue);
        else
            editor.putString(tag, value);
        editor.commit();
    }

    public static void Save(String tag, int value){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(tag, value);
        editor.commit();
    }

    public static String GetValue(String tag, String defaultValue){
        return prefs.getString(tag, defaultValue);
    }

    public static int GetValue(String tag, int defaultValue) {
        return prefs.getInt(tag, defaultValue);
    }

    public static class EditTextDialog extends DialogFragment implements OnEditorActionListener {

        private Pattern regex;
        private String key;
        private String defaultValue;

        private EditText mEditText;
        private TextView mTextView;
        private Button mSaveButton;
        private Button mCancelButton;

        public EditTextDialog() { }

        public static EditTextDialog newInstance(String message, String key,
                                                 String regex, String defaultValue) {
            EditTextDialog f = new EditTextDialog();
            Bundle args = new Bundle();
            args.putString("message", message);
            args.putString("key", key);
            args.putString("regex", regex);
            args.putString("defaultValue", defaultValue);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.fragment_edit_text, container);
            mEditText = (EditText) view.findViewById(R.id.value_text);
            mTextView = (TextView) view.findViewById(R.id.label_text);
            mTextView.setText(getArguments().getString("message"));
            mSaveButton = (Button) view.findViewById(R.id.btn_save);
            mCancelButton = (Button) view.findViewById(R.id.btn_cancel);

            regex = Pattern.compile(getArguments().getString("regex"));
            key = getArguments().getString("key");
            defaultValue = getArguments().getString("defaultValue");

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Save(key, mEditText.getText().toString(), defaultValue);
                    EditTextDialog.this.Close();
                }
            });
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditTextDialog.this.Close();
                }
            });
            String savedValue = GetValue(key, defaultValue);
            if(savedValue.trim() == "") savedValue = defaultValue;
            mEditText.setText(savedValue);
            mEditText.requestFocus();
            getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            mEditText.setOnEditorActionListener(this);
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                private String mPreviousText = "";

                @Override
                public void afterTextChanged(Editable s) {
                    if (regex.matcher(s).matches()) {
                        mPreviousText = s.toString();
                    } else {
                        s.replace(0, s.length(), mPreviousText);
                    }
                }
            });
            return view;
        }
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                Save(key, mEditText.getText().toString(), defaultValue);
                Close();
                return true;
            }
            return false;
        }
        public void Close(){this.dismiss();}
    }

    public static class SpinnerDialog extends DialogFragment implements OnEditorActionListener {

        private String key;

        private Spinner mSpinner;
        private TextView mTextView;
        private Button mSaveButton;
        private Button mCancelButton;

        public SpinnerDialog() { }

        public static SpinnerDialog newInstance(String message, String key,
                                                 String[] options, int defaultValue) {
            SpinnerDialog f = new SpinnerDialog();
            Bundle args = new Bundle();
            args.putString("message", message);
            args.putString("key", key);
            args.putStringArray("options", options);
            args.putInt("defaultValue", defaultValue);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.fragment_edit_dropdown, container);
            mSpinner = (Spinner) view.findViewById(R.id.value_spinner);
            mTextView = (TextView) view.findViewById(R.id.label_text);
            mTextView.setText(getArguments().getString("message"));
            mSaveButton = (Button) view.findViewById(R.id.btn_save);
            mCancelButton = (Button) view.findViewById(R.id.btn_cancel);

            String[] options = getArguments().getStringArray("options");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, options);
            mSpinner.setAdapter(adapter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            key = getArguments().getString("key");

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Save(key, mSpinner.getSelectedItemPosition());
                    SpinnerDialog.this.Close();
                }
            });
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpinnerDialog.this.Close();
                }
            });
            int savedValue = GetValue(key, getArguments().getInt("defaultValue"));
            if(savedValue >= 0 && savedValue < options.length)
                mSpinner.setSelection(savedValue);
            mSpinner.requestFocus();
            return view;
        }
        public void Close(){this.dismiss();}

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                Save(key, mSpinner.getSelectedItemPosition());
                Close();
                return true;
            }
            return false;
        }
    }

    public static class ConnectButtonFragment extends DialogFragment implements OnEditorActionListener {
        private boolean connected;
        private TextView mTextView;
        private Button mConnectButton;

        public ConnectButtonFragment() { }

        public static ConnectButtonFragment newInstance(String message) {
            ConnectButtonFragment f = new ConnectButtonFragment();
            Bundle args = new Bundle();
            args.putString("message", message);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.fragment_connect, container);
            mTextView = (TextView) view.findViewById(R.id.label_text);
            mTextView.setText(getArguments().getString("message"));
            mConnectButton = (Button) view.findViewById(R.id.btn_connect);
            mConnectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectButtonFragment.this.Connect();
                }
            });
            return view;
        }
        public void Close(){this.dismiss();}

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                Close();
                return true;
            }
            return false;
        }

        public void Connect(){
            connected = !connected;
            if(connected) {
                mTextView.setText("Connected");
                mTextView.setTextColor(Color.GREEN);
                mConnectButton.setText("Disconnect");
            }
            else {
                mTextView.setText("Not Connected");
                mTextView.setTextColor(Color.RED);
                mConnectButton.setText("Connect");
            }
        }
    }

    public static class BoundsFragment extends DialogFragment implements OnEditorActionListener {
        private static final int max = 180;
        private static final int min = 0;
        private static final int stringFormatLength = 3;
        private static final String stringFormat = "%03d";

        private TextView mTextView;
        private NumberPicker mXMinPicker;
        private NumberPicker mXMaxPicker;
        private NumberPicker mYMinPicker;
        private NumberPicker mYMaxPicker;
        private Button mSaveButton;
        private Button mCancelButton;

        private String key;
        private String defaultValue;

        public BoundsFragment() { }

        public static BoundsFragment newInstance(String message, String key, String defaultValue) {
            BoundsFragment f = new BoundsFragment();
            Bundle args = new Bundle();
            args.putString("message", message);
            args.putString("key", key);
            args.putString("defaultValue", defaultValue);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.fragment_edit_bounds, container);
            mTextView = (TextView) view.findViewById(R.id.label_text);
            mTextView.setText(getArguments().getString("message"));

            mSaveButton = (Button) view.findViewById(R.id.btn_save);
            mCancelButton = (Button) view.findViewById(R.id.btn_cancel);

            key = getArguments().getString("key");
            defaultValue = getArguments().getString("defaultValue");
            String savedValue = GetValue(key, defaultValue);

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Save(key, BoundsFragment.this.getStateString(), defaultValue);
                    BoundsFragment.this.Close();
                }
            });
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BoundsFragment.this.Close();
                }
            });

            mXMinPicker = (NumberPicker) view.findViewById(R.id.min_x);
            mXMinPicker.setMinValue(min);
            mXMinPicker.setMaxValue(max - 1);
            mXMinPicker.setTag("xmin");
            mXMinPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    BoundsFragment.this.ValueChanged(picker, oldVal, newVal);
                }
            });
            mXMaxPicker = (NumberPicker) view.findViewById(R.id.max_x);
            mXMaxPicker.setMinValue(min+1);
            mXMaxPicker.setMaxValue(max);
            mXMaxPicker.setTag("xmax");
            mXMaxPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    BoundsFragment.this.ValueChanged(picker, oldVal, newVal);
                }
            });
            mYMinPicker = (NumberPicker) view.findViewById(R.id.min_y);
            mYMinPicker.setMinValue(min);
            mYMinPicker.setMaxValue(max - 1);
            mYMinPicker.setTag("ymin");
            mYMinPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    BoundsFragment.this.ValueChanged(picker, oldVal, newVal);
                }
            });
            mYMaxPicker = (NumberPicker) view.findViewById(R.id.max_y);
            mYMaxPicker.setMinValue(min+1);
            mYMaxPicker.setMaxValue(max);
            mYMaxPicker.setTag("ymax");
            mYMaxPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    BoundsFragment.this.ValueChanged(picker, oldVal, newVal);
                }
            });

            setStateFromString(savedValue);

            return view;
        }
        public void Close(){this.dismiss();}

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                Save(key, getStateString(), defaultValue);
                Close();
                return true;
            }
            return false;
        }

        public String getStateString(){
            String stateString = "";
            int xmin = mXMinPicker.getValue();
            int xmax = mXMaxPicker.getValue();
            int ymin = mYMinPicker.getValue();
            int ymax = mYMaxPicker.getValue();

            stateString += String.format(stringFormat, xmin);
            stateString += String.format(stringFormat, xmax);
            stateString += String.format(stringFormat, ymin);
            stateString += String.format(stringFormat, ymax);

            return stateString;
        }

        public void setStateFromString(String stateString){
            if(stateString.length() != stringFormatLength * 4) return;
            int xmin = min;
            int xmax = max;
            int ymin = min;
            int ymax = max;
            try{
                xmin = Integer.valueOf(stateString.substring(0,stringFormatLength));
                xmax = Integer.valueOf(stateString.substring(stringFormatLength,stringFormatLength*2));
                ymin = Integer.valueOf(stateString.substring(stringFormatLength*2,stringFormatLength*3));
                ymax = Integer.valueOf(stateString.substring(stringFormatLength*3,stringFormatLength*4));
            }catch(NumberFormatException e){return;}
            if(xmin >= xmax) return;
            if(ymin >= ymax) return;
            if(xmin < min || ymin < min || xmax < (min+1) || ymax < (min+1)) return;
            if(xmin > (max-1) || ymin > (max-1) || xmax > max || ymax > max) return;
            mXMinPicker.setValue(xmin);
            mXMaxPicker.setValue(xmax);
            mYMinPicker.setValue(ymin);
            mYMaxPicker.setValue(ymax);
        }

        public void ValueChanged(NumberPicker picker, Integer oldVal, Integer newVal){
            String tag = (String)picker.getTag();
            switch(tag) {
                case "xmin":
                    if(newVal>=mXMaxPicker.getValue()) mXMaxPicker.setValue(newVal+1);
                    break;
                case "xmax":
                    if(newVal<=mXMinPicker.getValue()) mXMinPicker.setValue(newVal-1);
                    break;
                case "ymin":
                    if(newVal>=mYMaxPicker.getValue()) mYMaxPicker.setValue(newVal+1);
                    break;
                case "ymax":
                    if(newVal<=mYMinPicker.getValue()) mYMinPicker.setValue(newVal-1);
                    break;
                default:
                    break;
            }
        }
    }

    public static class InvertSwapFragment extends DialogFragment implements OnEditorActionListener {
        private TextView mTextView;
        private CheckBox mInvertXCheckbox;
        private CheckBox mInvertYCheckbox;
        private CheckBox mSwapCheckbox;
        private Button mSaveButton;
        private Button mCancelButton;

        private String key;
        private String defaultValue;

        public InvertSwapFragment() { }

        public static InvertSwapFragment newInstance(String message, String key, String defaultValue) {
            InvertSwapFragment f = new InvertSwapFragment();
            Bundle args = new Bundle();
            args.putString("message", message);
            args.putString("key", key);
            args.putString("defaultValue", defaultValue);
            f.setArguments(args);
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.fragment_edit_invertswap, container);
            mTextView = (TextView) view.findViewById(R.id.label_text);
            mTextView.setText(getArguments().getString("message"));

            mInvertXCheckbox = (CheckBox) view.findViewById(R.id.checkbox_invert_x);
            mInvertYCheckbox = (CheckBox) view.findViewById(R.id.checkbox_invert_y);
            mSwapCheckbox = (CheckBox) view.findViewById(R.id.checkbox_swap);

            mSaveButton = (Button) view.findViewById(R.id.btn_save);
            mCancelButton = (Button) view.findViewById(R.id.btn_cancel);

            key = getArguments().getString("key");
            defaultValue = getArguments().getString("defaultValue");
            String savedValue = GetValue(key, defaultValue);

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Save(key, InvertSwapFragment.this.getStateString(), defaultValue);
                    InvertSwapFragment.this.Close();
                }
            });
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InvertSwapFragment.this.Close();
                }
            });

            setStateFromString(savedValue);

            return view;
        }
        public void Close(){this.dismiss();}

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                Save(key, getStateString(), defaultValue);
                Close();
                return true;
            }
            return false;
        }

        public String getStateString(){
            String stateString = "";
            boolean invertXChecked = mInvertXCheckbox.isChecked();
            boolean invertYChecked = mInvertYCheckbox.isChecked();
            boolean swap = mSwapCheckbox.isChecked();

            stateString += invertXChecked?"1":"0";
            stateString += invertYChecked?"1":"0";
            stateString += swap?"1":"0";

            return stateString;
        }

        public void setStateFromString(String stateString){
            if(stateString.length() != 3) return;
            for(int i = 0; i < stateString.length();i++)
                if(!(stateString.charAt(i) == '0' || stateString.charAt(i) == '1'))
                    return;
            mInvertXCheckbox.setChecked(stateString.charAt(0) == '1');
            mInvertXCheckbox.setChecked(stateString.charAt(0) == '1');
            mSwapCheckbox.setChecked(stateString.charAt(0) == '1');
        }
    }
}