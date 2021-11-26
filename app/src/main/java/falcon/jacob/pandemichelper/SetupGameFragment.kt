package falcon.jacob.pandemichelper

import android.R.attr.onClick
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class SetupGameFragment : Fragment() {

    var settingsView : LinearLayout?=null;
    private var seekBar : SeekBar?=null;
    private var epidemicTextView : TextView?=null;
    var playerSpinner : Spinner?=null;
    var playButton : Button?=null;
    var playerCount = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeVariables(view);
        seekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            var progress = 0;
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
                 this.progress = progress;
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                setEpidemicTextViewText(progress + EpidemicLevel.BEGINNER.value);
            }
        })

        playerSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updatePlayerTextViews(position + 2);
            }

        }

        playButton?.setOnClickListener(View.OnClickListener {
            //validate settings

        })

        view.findViewById<Button>(R.id.play_button).setOnClickListener {
            findNavController().navigate(R.id.action_SetupGame_to_gameActivity)
        }
    }

    private fun updatePlayerTextViews(count: Int){
        if(count > playerCount){
            do {
                playerCount++;
                initializePlayerRow(playerCount);
            }while(count != playerCount)
        } else if(count < playerCount){
            //remove views
            do {
                settingsView?.removeViewAt(playerCount);
                playerCount--;
            }while(count != playerCount)
        }
    }

    private fun setEpidemicTextViewText(setting: Int){
        val level = EpidemicLevel.fromInt(setting);
        epidemicTextView?.text = getString(R.string.epidemic_text_view, setting, level);
    }

    private fun initializeVariables(view: View) {
        settingsView = view.findViewById(R.id.settings_layout_view) as LinearLayout;
        seekBar = view.findViewById(R.id.epidemic_count_seek_bar) as SeekBar
        playButton = view.findViewById(R.id.play_button) as Button;
        epidemicTextView = view.findViewById(R.id.epidemic_text_view) as TextView
        setEpidemicTextViewText((EpidemicLevel.BEGINNER.value));
        playerSpinner = view.findViewById(R.id.player_count_spinner);
        initializePlayerRow(1);
        playerCount++;
        initializePlayerRow(2);
        playerCount++;
    }

    private fun initializePlayerRow(count: Int){
        val playerRow = LinearLayout(this.context);
        val padding = context?.resources?.getDimensionPixelSize(R.dimen.player_row_padding) ?: 0;
        val bottomMargin = context?.resources?.getDimensionPixelSize(R.dimen.player_row_bottom_margin) ?: 0;
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        playerRow.id = ViewCompat.generateViewId();
        playerRow.orientation = LinearLayout.HORIZONTAL;
        playerRow.setPadding(padding,0,padding,0);
        layoutParams.bottomMargin = bottomMargin;
        playerRow.layoutParams = layoutParams;

        val playerTextView = TextView(this.context);
        val layoutParamsText = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        playerTextView.id = ViewCompat.generateViewId();
        playerTextView.text = getString(R.string.player_name_text, count);
        playerTextView.textSize = 18F;
        playerTextView.setTextColor(this.resources.getColor(R.color.colorPrimaryText));
        playerTextView.layoutParams = layoutParamsText
        playerRow.addView(playerTextView);

        val playerEditText = EditText(this.context);
        val layoutParamsEdit = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1F);
        playerEditText.id = ViewCompat.generateViewId();
        playerEditText.hint = getString(R.string.player_name_placeholder_text);
        playerEditText.layoutParams = layoutParamsEdit;
        val filterArray = arrayOfNulls<InputFilter>(1);
        filterArray[0] = LengthFilter(20);
        playerEditText.filters = filterArray;
        playerRow.addView(playerEditText);

        settingsView?.addView(playerRow, count);
    }

    private fun validateSettings(){

    }
}