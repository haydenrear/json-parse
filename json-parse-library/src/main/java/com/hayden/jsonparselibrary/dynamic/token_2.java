import java.io.Serializable;
import java.util.Map;

public class token_2 implements Serializable {
  public String futuresType;
  
  public rateLimits[] rateLimits;
  
  public assets[] assets;
  
  public String[] exchangeFilters;
  
  public String timezone;
  
  public Long serverTime;
  
  public symbols[] symbols;
  
  public static class rateLimits implements Serializable {
    public Long intervalNum;
    
    public Long limit;
    
    public String interval;
    
    public String rateLimitType;
  }
  
  public static class assets implements Serializable {
    public String autoAssetExchange;
    
    public String asset;
    
    public Boolean marginAvailable;
  }
  
  public static class symbols implements Serializable {
    public Long quantityPrecision;
    
    public String symbol;
    
    public Long pricePrecision;
    
    public String requiredMarginPercent;
    
    public String contractType;
    
    public Long onboardDate;
    
    public String baseAsset;
    
    public Map[] filters;
    
    public Long baseAssetPrecision;
    
    public Long settlePlan;
    
    public String pair;
    
    public String triggerProtect;
    
    public String marginAsset;
    
    public Long quotePrecision;
    
    public String underlyingType;
    
    public String[] orderTypes;
    
    public String maintMarginPercent;
    
    public Long deliveryDate;
    
    public String[] timeInForce;
    
    public String quoteAsset;
    
    public String status;
    
    public String[] underlyingSubType;
  }
}
