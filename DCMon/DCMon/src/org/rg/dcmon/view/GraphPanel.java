/**
 * @(#) GraphPanel.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.view;


import org.rg.dcmon.monitor.*;
import org.rg.dcmon.session.*;
import java.awt.*;
import javax.swing.JPanel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Date;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.*;
import java.awt.geom.*;
import javax.swing.BorderFactory;
import java.awt.event.*;
 
public abstract class GraphPanel extends JPanel implements ComponentListener{
	protected int MAX_TIMESERIES_ITEMS = 100;
	protected int MAX_TIMESERIES_AGE = 10000;
	public static int WIDTH = 300;
	public static int HEIGHT = 200;
		
	protected View view;
	protected JFreeChart chart; 
	protected ChartPanel chartPanel;
	protected TimeSeries timeseries;
	protected TimeSeriesCollection dataset;
	protected long lastRefreshTime;
	protected long minWaitTime;
	protected String xLabel;
	protected String yLabel;
	
	public GraphPanel(String xAxis, String yAxis, String title){
		super(null, true);
		xLabel = xAxis;
		yLabel = yAxis;
		init();
	}
	
	public void init()
	{   	
		//FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
		//this.setLayout(layout);
		//this.setBackground(Color.BLACK);
		
		this.setSize(new Dimension(WIDTH, HEIGHT));
		//this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		//this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setVisible(true);
		this.setOpaque(true);
		this.addComponentListener(this);
				
		minWaitTime = SettingsManager.getInstance().updateInterval;
	}
	
	public void initGraph(Session session)
	{
		timeseries = new TimeSeries("", Millisecond.class);
		timeseries.setMaximumItemCount(MAX_TIMESERIES_ITEMS);
		timeseries.setMaximumItemAge(10000);
		dataset = new TimeSeriesCollection();
		dataset.addSeries(timeseries);

		DateAxis domain = new DateAxis(xLabel);
		NumberAxis range = new NumberAxis(yLabel);
		
		domain.setTickLabelFont(new Font("Arial",Font.PLAIN,10));
		range.setTickLabelFont(new Font("Arial",Font.PLAIN,10));
		domain.setLabelFont(new Font("Arial",Font.PLAIN,12));
		range.setLabelFont(new Font("Arial",Font.PLAIN,12));
		
		XYItemRenderer renderer = new DefaultXYItemRenderer();
		renderer.setSeriesPaint(0,Color.green);
		renderer.setSeriesPaint(1,Color.red);
		renderer.setStroke(new BasicStroke(1f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL));
		//Shape shape = new Shape();
		//renderer.setShape(shape);
		//Rectangle shape = new Rectangle();

		GeneralPath rect = new GeneralPath();
		rect.moveTo(0.0f, 1.0f);
		rect.lineTo(0.0f, -1.0f);
		rect.lineTo(2.0f, -1.0f);
		rect.lineTo(2.0f, 1.0f);
		rect.lineTo(0.0f, 1.0f);
		rect.closePath();
		renderer.setSeriesShape(0, rect); 
		
		XYPlot plot = new XYPlot(dataset,domain,range,renderer);
		//plot.setBackgroundPaint(Color.lightGray);
		//plot.setDomainGridlinePaint(Color.white);
		//plot.setRangeGridlinePaint(Color.white);
		//domain.plot.setAxisOffset(newSpacer(Spacer.ABSOLUTE,5.0,5.0,5.0,5.0));
		domain.setAutoRange(true);
		domain.setLowerMargin(0.0);
		domain.setUpperMargin(0.0);
		domain.setTickLabelsVisible(true);
		range.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		JFreeChart chart = new JFreeChart(yLabel, new Font("Arial",Font.BOLD,12), plot,	false);

		chartPanel = new ChartPanel(chart);
		chartPanel.setSize(this.getSize());
		//chartPanel.setMinimumSize(this.getSize());
		//chartPanel.setPreferredSize(this.getSize());

		chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		 
		RelativeDateFormat rdf = new RelativeDateFormat(session.getStartTime().getTime());
        rdf.setSecondFormatter(new DecimalFormat("00"));
        domain.setDateFormatOverride(rdf); 
        
        /*GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constr = new GridBagConstraints();
		constr.fill = GridBagConstraints.BOTH;
		layout.setConstraints(chartPanel, constr);
		setLayout(layout);*/
		
		add(chartPanel);
	}
	
	
	public abstract void refresh(Session session);
		
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentResized(ComponentEvent arg0) {
		//System.out.println(arg0.toString());
		//arg0.
		chartPanel.setSize(this.getSize());
	}

	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	public class RelativeDateFormat extends DateFormat {
		   
	    /** The base milliseconds for the elapsed time calculation. */
	    private long baseMillis;
	   
	    /**
	     * A flag that controls whether or not a zero day count is displayed.
	     */
	    private boolean showZeroDays;
	   
	    /**
	     * A formatter for the day count (most likely not critical until the
	     * day count exceeds 999).
	     */
	    private NumberFormat dayFormatter;
	   
	    /**
	     * A string appended after the day count.
	     */
	    private String daySuffix;
	   
	    /**
	     * A string appended after the hours.
	     */
	    private String hourSuffix;
	   
	    /**
	     * A string appended after the minutes.
	     */
	    private String minuteSuffix;
	   
	    /**
	     * A formatter for the seconds (and milliseconds).
	     */
	    private NumberFormat secondFormatter;
	   
	    /**
	     * A string appended after the seconds.
	     */
	    private String secondSuffix;

	    /**
	     * A constant for the number of milliseconds in one hour.
	     */
	    private long MILLISECONDS_IN_ONE_HOUR = 60 * 60 * 1000L;

	    /**
	     * A constant for the number of milliseconds in one day.
	     */
	    private long MILLISECONDS_IN_ONE_DAY = 24 * MILLISECONDS_IN_ONE_HOUR;
	   
	    /**
	     * Creates a new instance.
	     */
	    public RelativeDateFormat() {
	        this(0L); 
	    }
	   
	    /**
	     * Creates a new instance.
	     *
	     * @param time  the date/time (<code>null</code> not permitted).
	     */
	    public RelativeDateFormat(Date time) {
	        this(time.getTime());
	    }
	   
	    /**
	     * Creates a new instance.
	     *
	     * @param baseMillis  the time zone (<code>null</code> not permitted).
	     */
	    public RelativeDateFormat(long baseMillis) {
	        super();
	        this.baseMillis = baseMillis;
	        this.showZeroDays = false;
	        this.dayFormatter = NumberFormat.getInstance();
	        this.daySuffix = "d";
	        this.hourSuffix = "h";
	        this.minuteSuffix = "m";
	        this.secondFormatter = NumberFormat.getNumberInstance();
	        this.secondFormatter.setMaximumFractionDigits(3);
	        this.secondFormatter.setMinimumFractionDigits(3);
	        this.secondSuffix = "s";
	    }
	   
	    /**
	     * Returns the base date/time used to calculate the elapsed time for
	     * display.
	     *
	     * @return The base date/time in milliseconds since 1-Jan-1970.
	     *
	     * @see #setBaseMillis(long)
	     */
	    public long getBaseMillis() {
	        return this.baseMillis;
	    }
	   
	    /**
	     * Sets the base date/time used to calculate the elapsed time for display. 
	     * This should be specified in milliseconds using the same encoding as
	     * <code>java.util.Date</code>.
	     *
	     * @param baseMillis  the base date/time in milliseconds.
	     *
	     * @see #getBaseMillis()
	     */
	    public void setBaseMillis(long baseMillis) {
	        this.baseMillis = baseMillis;
	    }
	   
	    /**
	     * Returns the flag that controls whether or not zero day counts are
	     * shown in the formatted output.
	     *
	     * @return The flag.
	     *
	     * @see #setShowZeroDays(boolean)
	     */
	    public boolean getShowZeroDays() {
	        return this.showZeroDays;
	    }
	   
	    /**
	     * Sets the flag that controls whether or not zero day counts are shown
	     * in the formatted output.
	     *
	     * @param show  the flag.
	     *
	     * @see #getShowZeroDays()
	     */
	    public void setShowZeroDays(boolean show) {
	        this.showZeroDays = show;
	    }
	   
	    /**
	     * Returns the string that is appended to the day count.
	     *
	     * @return The string.
	     *
	     * @see #setDaySuffix(String)
	     */
	    public String getDaySuffix() {
	        return this.daySuffix;
	    }
	   
	    /**
	     * Sets the string that is appended to the day count.
	     *
	     * @param suffix  the suffix.
	     *
	     * @see #getDaySuffix()
	     */
	    public void setDaySuffix(String suffix) {
	        this.daySuffix = suffix;
	    }

	    /**
	     * Returns the string that is appended to the hour count.
	     *
	     * @return The string.
	     *
	     * @see #setHourSuffix(String)
	     */
	    public String getHourSuffix() {
	        return this.hourSuffix;
	    }
	   
	    /**
	     * Sets the string that is appended to the hour count.
	     *
	     * @param suffix  the suffix.
	     *
	     * @see #getHourSuffix()
	     */
	    public void setHourSuffix(String suffix) {
	        this.hourSuffix = suffix;
	    }

	    /**
	     * Returns the string that is appended to the minute count.
	     *
	     * @return The string.
	     *
	     * @see #setMinuteSuffix(String)
	     */
	    public String getMinuteSuffix() {
	        return this.minuteSuffix;
	    }
	   
	    /**
	     * Sets the string that is appended to the minute count.
	     *
	     * @param suffix  the suffix.
	     *
	     * @see #getMinuteSuffix()
	     */
	    public void setMinuteSuffix(String suffix) {
	        this.minuteSuffix = suffix;
	    }

	    /**
	     * Returns the string that is appended to the second count.
	     *
	     * @return The string.
	     *
	     * @see #setSecondSuffix(String)
	     */
	    public String getSecondSuffix() {
	        return this.secondSuffix;
	    }
	   
	    /**
	     * Sets the string that is appended to the second count.
	     *
	     * @param suffix  the suffix.
	     *
	     * @see #getSecondSuffix()
	     */
	    public void setSecondSuffix(String suffix) {
	        this.secondSuffix = suffix;
	    }
	   
	    /**
	     * Sets the formatter for the seconds and milliseconds.
	     *
	     * @param formatter  the formatter (<code>null</code> not permitted).
	     */
	    public void setSecondFormatter(NumberFormat formatter) {
	        if (formatter == null) {
	            throw new IllegalArgumentException("Null 'formatter' argument.");
	        }
	        this.secondFormatter = formatter;
	    }

	    /**
	     * Formats the given date as the amount of elapsed time (relative to the
	     * base date specified in the constructor).
	     *
	     * @param date  the date.
	     * @param toAppendTo  the string buffer.
	     * @param fieldPosition  the field position.
	     *
	     * @return The formatted date.
	     */
	    public StringBuffer format(Date date, StringBuffer toAppendTo,
	                               FieldPosition fieldPosition) {
	        long currentMillis = date.getTime();
	        long elapsed = currentMillis - baseMillis;
	       
	        long days = elapsed / MILLISECONDS_IN_ONE_DAY;
	        elapsed = elapsed - (days * MILLISECONDS_IN_ONE_DAY);
	        long hours = elapsed / MILLISECONDS_IN_ONE_HOUR;
	        elapsed = elapsed - (hours * MILLISECONDS_IN_ONE_HOUR);
	        long minutes = elapsed / 60000L;
	        elapsed = elapsed - (minutes * 60000L);
	        double seconds = elapsed / 1000.0;
	        if (days != 0 || this.showZeroDays) {
	            toAppendTo.append(this.dayFormatter.format(days) + getDaySuffix());
	        }
	        toAppendTo.append(String.valueOf(hours) + getHourSuffix());
	        toAppendTo.append(String.valueOf(minutes) + getMinuteSuffix());
	        toAppendTo.append(this.secondFormatter.format(seconds)
	                + getSecondSuffix());
	        return toAppendTo;   
	    }

	    /**
	     * Parses the given string (not implemented).
	     *
	     * @param source  the date string.
	     * @param pos  the parse position.
	     *
	     * @return <code>null</code>, as this method has not been implemented.
	     */
	    public Date parse(String source, ParsePosition pos) {
	        return null;   
	    }

	    /**
	     * Tests this formatter for equality with an arbitrary object.
	     *
	     * @param obj  the object.
	     *
	     * @return A boolean.
	     */
	    public boolean equals(Object obj) {
	        if (obj == this) {
	            return true;
	        }
	        if (!(obj instanceof RelativeDateFormat)) {
	            return false;
	        }
	        if (!super.equals(obj)) {
	            return false;
	        }
	        RelativeDateFormat that = (RelativeDateFormat) obj;
	        if (this.baseMillis != that.baseMillis) {
	            return false;
	        }
	        return true;
	    }
	}
	
}
