package com.zoltu.gae_authentication.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window.Location;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Extends DefaultRequestTransport to handle the authentication failures reported by
 * {@link com.zoltu.gae_authentication.server.AuthenticationFilter}
 */
public class AuthenticationRequestTransport extends DefaultRequestTransport
{
	private final EventBus mEventBus;
	
	public AuthenticationRequestTransport(EventBus pEventBus)
	{
		mEventBus = pEventBus;
		new ReloadOnAuthenticationFailure().register(mEventBus);
	}
	
	/**
	 * Called when the transport sends out a request to the server.
	 * 
	 * @param pReceiver
	 *            The transport receiver that the server's response will come in on.
	 * @return The RequestCallback to call when a response from the server is received.
	 */
	@Override
	protected com.google.gwt.http.client.RequestCallback createRequestCallback(final TransportReceiver pTransportReceiver)
	{
		/**
		 * Keep track of the super callback so we can fall back on it when we don't want to handle the request.
		 */
		final com.google.gwt.http.client.RequestCallback lSuperCallback = super.createRequestCallback(pTransportReceiver);
		
		return new RequestCallback(lSuperCallback, pTransportReceiver);
	}
	
	/**
	 * Created when a request is sent out to the server, called when a response to that request is received.
	 */
	private class RequestCallback implements com.google.gwt.http.client.RequestCallback
	{
		/**
		 * The RequestCallback from {@link DefaultRequestTransport}. We can fall back on it when we don't want to handle
		 * a particular request.
		 */
		final com.google.gwt.http.client.RequestCallback mSuperCallback;
		/** 
		 * The {@link TransportReceiver} on which the response is received.
		 */
		final TransportReceiver mTransportReceiver;
		
		/**
		 * Constructor.
		 */
		public RequestCallback(com.google.gwt.http.client.RequestCallback pSuperCallback, final TransportReceiver pTransportReceiver)
		{
			mSuperCallback = pSuperCallback;
			mTransportReceiver = pTransportReceiver;
		}
		
		/**
		 * Called when a response is received from the server.
		 */
		public void onResponseReceived(Request pRequest, Response pResponse)
		{
			/*
			 * The AuthenticationFilter filter responds with Response.SC_UNAUTHORIZED and adds a "login" url header if the
			 * user is not logged in. When we receive that combo, post an event so that the app can handle things as it
			 * sees fit.
			 */
			
			if (Response.SC_UNAUTHORIZED == pResponse.getStatusCode())
			{
				String lLoginUrl = pResponse.getHeader("login");
				if (lLoginUrl != null)
				{
					/*
					 * Hand the receiver a non-fatal callback, so that com.google.web.bindery.requestfactory.shared.Receiver
					 * will not post a runtime exception.
					 */
					mTransportReceiver.onTransportFailure(new ServerFailure("Unauthenticated user", null, null, false));
					mEventBus.fireEvent(new AuthenticationFailureEvent(lLoginUrl));
					return;
				}
			}
			mSuperCallback.onResponseReceived(pRequest, pResponse);
		}
		
		/**
		 * Called when an error is received from the server.
		 */
		public void onError(Request pRequest, Throwable pException)
		{
			mSuperCallback.onError(pRequest, pException);
		}
	}
	
	/**
	 * Implemented by handlers of this type of event.
	 */
	public interface AuthenticationFailureHandler extends EventHandler
	{
		/**
		 * Called when an {@link AuthenticationFailureEvent} is fired.
		 * 
		 * @param requestEvent
		 *            a {@link AuthenticationFailureEvent} instance
		 */
		void onAuthFailure(AuthenticationFailureEvent pRequestEvent);
	}
	
	/**
	 * A minimal authentication failure handler which takes the user a login page.
	 */
	public class ReloadOnAuthenticationFailure implements AuthenticationFailureHandler
	{
		public HandlerRegistration register(EventBus pEventBus)
		{
			return AuthenticationFailureEvent.register(pEventBus, this);
		}
		
		public void onAuthFailure(AuthenticationFailureEvent pRequestEvent)
		{
			Location.replace(pRequestEvent.getLoginUrl());
		}
	}
	
	/**
	 * An event posted when an authentication failure is detected.
	 */
	public static class AuthenticationFailureEvent extends GwtEvent<AuthenticationFailureHandler>
	{
		private static final Type<AuthenticationFailureHandler> TYPE = new Type<AuthenticationFailureHandler>();
		
		/**
		 * Register a {@link AuthenticationFailureHandler} on an {@link EventBus}.
		 * 
		 * @param eventBus
		 *            the {@link EventBus}
		 * @param handler
		 *            a {@link AuthenticationFailureHandler}
		 * @return a {@link HandlerRegistration} instance
		 */
		public static HandlerRegistration register(EventBus pEventBus, AuthenticationFailureHandler pHandler)
		{
			return pEventBus.addHandler(TYPE, pHandler);
		}
		
		/**
		 * Will only be non-null if this is an event of type {@link State#RECEIVED}, and the RPC was successful.
		 */
		private final String mLoginUrl;
		
		/**
		 * Constructs a new @{link RequestEvent}.
		 * 
		 * @param state
		 *            a {@link State} instance
		 * @param response
		 *            a {@link Response} instance
		 */
		public AuthenticationFailureEvent(String pLoginUrl)
		{
			mLoginUrl = pLoginUrl;
		}
		
		@Override
		public GwtEvent.Type<AuthenticationFailureHandler> getAssociatedType()
		{
			return TYPE;
		}
		
		/**
		 * Returns the URL the user can visit to reauthenticate.
		 * 
		 * @return a {@link Response} instance
		 */
		public String getLoginUrl()
		{
			return mLoginUrl;
		}
		
		@Override
		protected void dispatch(AuthenticationFailureHandler pHandler)
		{
			pHandler.onAuthFailure(this);
		}
	}
}
