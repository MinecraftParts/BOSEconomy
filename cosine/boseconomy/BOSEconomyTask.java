package cosine.boseconomy;

class BOSEconomyTask
  implements Runnable
{
  private BOSEconomy plugin;
  private long lastSave;
  
  public BOSEconomyTask(BOSEconomy plugin)
  {
    this.plugin = plugin;
    this.lastSave = System.currentTimeMillis();
  }
  
  public void run()
  {
    try
    {
      long currentTime = System.currentTimeMillis();
      

      this.plugin.getBracketManager().updateOnlineTimes(currentTime);
      

      this.plugin.getBracketManager().buildPaySession(currentTime).run();
      if ((this.plugin.getSettingsManager().getSaveInterval().getSeconds() > 0) && (
        (this.lastSave == 0L) || 
        ((currentTime - this.lastSave) / 1000L >= this.plugin.getSettingsManager().getSaveInterval().getSeconds())))
      {
        this.plugin.getBOSEDatabase().commitManagers();
        this.plugin.getBOSEDatabase().commit();
        this.lastSave = currentTime;
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      this.plugin.sendConsoleMessage(BOSEconomy.TAG_WARNING_COLOR + 
        "Unhandled exception generated by the BOSEconomy task! Report this to the developer!");
    }
  }
  
  public void promptSave()
  {
    this.lastSave = 0L;
  }
}