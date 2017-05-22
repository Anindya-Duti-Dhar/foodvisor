package foodvisor.com.foodvisor.utils;


import org.scribe.builder.api.DefaultApi10a;

public class WooCommerceApi extends DefaultApi10a {

    @Override
    public org.scribe.model.Verb getRequestTokenVerb()
    {
        return org.scribe.model.Verb.POST;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://woocommerce.cloudaccess.host/wc-auth/authorize";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "none";
    }

    @Override
    public String getAuthorizationUrl(org.scribe.model.Token requestToken) {
        return "none";
    }
}