/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2017 Serge Rider (serge@jkiss.org)
 * Copyright (C) 2011-2012 Eugene Fradkin (eugene.fradkin@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkiss.dbeaver.ui.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.jkiss.dbeaver.DBeaverPreferences;
import org.jkiss.dbeaver.ModelPreferences;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.model.data.DBDDisplayFormat;
import org.jkiss.dbeaver.model.preferences.DBPPreferenceStore;
import org.jkiss.dbeaver.ui.UIUtils;
import org.jkiss.dbeaver.ui.controls.resultset.ValueFormatSelector;
import org.jkiss.dbeaver.ui.controls.resultset.spreadsheet.Spreadsheet;
import org.jkiss.dbeaver.utils.PrefUtils;
import org.jkiss.utils.CommonUtils;
import org.jkiss.dbeaver.core.CoreMessages;

/**
 * PrefPageResultSetPresentation
 */
public class PrefPageResultSetPresentation extends TargetPrefPage
{
    public static final String PAGE_ID = "org.jkiss.dbeaver.preferences.main.resultset.presentation"; //$NON-NLS-1$

    private Button gridShowOddRows;
    private Button rightJustifyNumbers;
    private Button transformComplexTypes;
    private Spinner gridRowBatchSize;
    private Button gridShowCellIcons;
    private Combo gridDoubleClickBehavior;
    private Button autoSwitchMode;
    private Button showDescription;
    private Button showConnectionName;

    private Spinner textMaxColumnSize;
    private ValueFormatSelector textValueFormat;

    public PrefPageResultSetPresentation()
    {
        super();
    }

    @Override
    protected boolean hasDataSourceSpecificOptions(DBPDataSourceContainer dataSourceDescriptor)
    {
        DBPPreferenceStore store = dataSourceDescriptor.getPreferenceStore();
        return
            store.contains(DBeaverPreferences.RESULT_SET_SHOW_ODD_ROWS) ||
            store.contains(DBeaverPreferences.RESULT_SET_RIGHT_JUSTIFY_NUMBERS) ||
            store.contains(ModelPreferences.RESULT_TRANSFORM_COMPLEX_TYPES) ||
            store.contains(DBeaverPreferences.RESULT_SET_SHOW_CELL_ICONS) ||
            store.contains(DBeaverPreferences.RESULT_SET_SHOW_DESCRIPTION) ||
            store.contains(DBeaverPreferences.RESULT_SET_SHOW_CONNECTION_NAME) ||
            store.contains(DBeaverPreferences.RESULT_SET_DOUBLE_CLICK) ||
            store.contains(DBeaverPreferences.RESULT_SET_AUTO_SWITCH_MODE) ||
            store.contains(DBeaverPreferences.RESULT_SET_ROW_BATCH_SIZE) ||
            store.contains(DBeaverPreferences.RESULT_TEXT_MAX_COLUMN_SIZE) ||
            store.contains(DBeaverPreferences.RESULT_TEXT_VALUE_FORMAT)
            ;
    }

    @Override
    protected boolean supportsDataSourceSpecificOptions()
    {
        return true;
    }

    @Override
    protected Control createPreferenceContent(Composite parent)
    {
        Composite composite = UIUtils.createPlaceholder(parent, 1, 5);

        {
            Group uiGroup = UIUtils.createControlGroup(composite, CoreMessages.pref_page_database_resultsets_group_common, 1, SWT.NONE, 0);
            autoSwitchMode = UIUtils.createCheckbox(uiGroup, CoreMessages.pref_page_database_resultsets_label_switch_mode_on_rows, false);
            showDescription = UIUtils.createCheckbox(uiGroup, CoreMessages.pref_page_database_resultsets_label_show_column_description, false);
            showConnectionName = UIUtils.createCheckbox(uiGroup, CoreMessages.pref_page_database_resultsets_label_show_connection_name, false);
            transformComplexTypes = UIUtils.createCheckbox(uiGroup, CoreMessages.pref_page_database_resultsets_label_structurize_complex_types, CoreMessages.pref_page_database_resultsets_label_structurize_complex_types_tip, false, 1);
        }

        {
            Group uiGroup = UIUtils.createControlGroup(composite, CoreMessages.pref_page_database_resultsets_group_grid, 2, SWT.NONE, 0);

            gridShowOddRows = UIUtils.createCheckbox(uiGroup, CoreMessages.pref_page_database_resultsets_label_mark_odd_rows, null, false, 2);
            rightJustifyNumbers = UIUtils.createCheckbox(uiGroup, CoreMessages.pref_page_database_resultsets_label_right_justify_numbers_and_date, null, false, 2);
            gridRowBatchSize = UIUtils.createLabelSpinner(uiGroup, CoreMessages.pref_page_database_resultsets_label_row_batch_size, 1, 1, Short.MAX_VALUE);
            gridShowCellIcons = UIUtils.createCheckbox(uiGroup, CoreMessages.pref_page_database_resultsets_label_show_cell_icons, null, false, 2);
            gridDoubleClickBehavior = UIUtils.createLabelCombo(uiGroup, CoreMessages.pref_page_database_resultsets_label_double_click_behavior, SWT.READ_ONLY);
            gridDoubleClickBehavior.add("None", Spreadsheet.DoubleClickBehavior.NONE.ordinal());
            gridDoubleClickBehavior.add("Editor", Spreadsheet.DoubleClickBehavior.EDITOR.ordinal());
            gridDoubleClickBehavior.add("Inline Editor", Spreadsheet.DoubleClickBehavior.INLINE_EDITOR.ordinal());
        }

        {
            Group uiGroup = UIUtils.createControlGroup(composite, CoreMessages.pref_page_database_resultsets_group_plain_text, 2, SWT.NONE, 0);

            textMaxColumnSize = UIUtils.createLabelSpinner(uiGroup, CoreMessages.pref_page_database_resultsets_label_maximum_column_length, 0, 10, Integer.MAX_VALUE);
            textValueFormat = new ValueFormatSelector(uiGroup);
        }

        return composite;
    }

    @Override
    protected void loadPreferences(DBPPreferenceStore store)
    {
        try {
            gridShowOddRows.setSelection(store.getBoolean(DBeaverPreferences.RESULT_SET_SHOW_ODD_ROWS));
            rightJustifyNumbers.setSelection(store.getBoolean(DBeaverPreferences.RESULT_SET_RIGHT_JUSTIFY_NUMBERS));
            transformComplexTypes.setSelection(store.getBoolean(ModelPreferences.RESULT_TRANSFORM_COMPLEX_TYPES));

            gridRowBatchSize.setSelection(store.getInt(DBeaverPreferences.RESULT_SET_ROW_BATCH_SIZE));
            gridShowCellIcons.setSelection(store.getBoolean(DBeaverPreferences.RESULT_SET_SHOW_CELL_ICONS));
            gridDoubleClickBehavior.select(
                Spreadsheet.DoubleClickBehavior.valueOf(store.getString(DBeaverPreferences.RESULT_SET_DOUBLE_CLICK)).ordinal());
            autoSwitchMode.setSelection(store.getBoolean(DBeaverPreferences.RESULT_SET_AUTO_SWITCH_MODE));
            showDescription.setSelection(store.getBoolean(DBeaverPreferences.RESULT_SET_SHOW_DESCRIPTION));
            showConnectionName.setSelection(store.getBoolean(DBeaverPreferences.RESULT_SET_SHOW_CONNECTION_NAME));

            textMaxColumnSize.setSelection(store.getInt(DBeaverPreferences.RESULT_TEXT_MAX_COLUMN_SIZE));
            textValueFormat.select(DBDDisplayFormat.safeValueOf(store.getString(DBeaverPreferences.RESULT_TEXT_VALUE_FORMAT)));
        } catch (Exception e) {
            log.warn(e);
        }
    }

    @Override
    protected void savePreferences(DBPPreferenceStore store)
    {
        try {
            store.setValue(DBeaverPreferences.RESULT_SET_SHOW_ODD_ROWS, gridShowOddRows.getSelection());
            store.setValue(DBeaverPreferences.RESULT_SET_RIGHT_JUSTIFY_NUMBERS, rightJustifyNumbers.getSelection());
            store.setValue(ModelPreferences.RESULT_TRANSFORM_COMPLEX_TYPES, transformComplexTypes.getSelection());
            store.setValue(DBeaverPreferences.RESULT_SET_ROW_BATCH_SIZE, gridRowBatchSize.getSelection());
            store.setValue(DBeaverPreferences.RESULT_SET_SHOW_CELL_ICONS, gridShowCellIcons.getSelection());
            store.setValue(DBeaverPreferences.RESULT_SET_DOUBLE_CLICK, CommonUtils.fromOrdinal(Spreadsheet.DoubleClickBehavior.class, gridDoubleClickBehavior.getSelectionIndex()).name());
            store.setValue(DBeaverPreferences.RESULT_SET_AUTO_SWITCH_MODE, autoSwitchMode.getSelection());
            store.setValue(DBeaverPreferences.RESULT_SET_SHOW_DESCRIPTION, showDescription.getSelection());
            store.setValue(DBeaverPreferences.RESULT_SET_SHOW_CONNECTION_NAME, showConnectionName.getSelection());
            store.setValue(DBeaverPreferences.RESULT_TEXT_MAX_COLUMN_SIZE, textMaxColumnSize.getSelection());
            store.setValue(DBeaverPreferences.RESULT_TEXT_VALUE_FORMAT, textValueFormat.getSelection().name());
        } catch (Exception e) {
            log.warn(e);
        }
        PrefUtils.savePreferenceStore(store);
    }

    @Override
    protected void clearPreferences(DBPPreferenceStore store)
    {
        store.setToDefault(DBeaverPreferences.RESULT_SET_SHOW_ODD_ROWS);
        store.setToDefault(DBeaverPreferences.RESULT_SET_RIGHT_JUSTIFY_NUMBERS);
        store.setToDefault(ModelPreferences.RESULT_TRANSFORM_COMPLEX_TYPES);

        store.setToDefault(DBeaverPreferences.RESULT_SET_ROW_BATCH_SIZE);
        store.setToDefault(DBeaverPreferences.RESULT_SET_SHOW_CELL_ICONS);
        store.setToDefault(DBeaverPreferences.RESULT_SET_DOUBLE_CLICK);
        store.setToDefault(DBeaverPreferences.RESULT_SET_AUTO_SWITCH_MODE);
        store.setToDefault(DBeaverPreferences.RESULT_SET_SHOW_DESCRIPTION);
        store.setToDefault(DBeaverPreferences.RESULT_SET_SHOW_CONNECTION_NAME);
        store.setToDefault(DBeaverPreferences.RESULT_TEXT_MAX_COLUMN_SIZE);
        store.setToDefault(DBeaverPreferences.RESULT_TEXT_VALUE_FORMAT);
    }

    @Override
    protected String getPropertyPageID()
    {
        return PAGE_ID;
    }

}
