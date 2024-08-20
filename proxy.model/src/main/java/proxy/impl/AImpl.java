/**
 */
package proxy.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import proxy.A;
import proxy.ProxyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>A</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link proxy.impl.AImpl#getNoProxyNoCon <em>No Proxy No Con</em>}</li>
 *   <li>{@link proxy.impl.AImpl#getNoProxyCon <em>No Proxy Con</em>}</li>
 *   <li>{@link proxy.impl.AImpl#getProxyNoCon <em>Proxy No Con</em>}</li>
 *   <li>{@link proxy.impl.AImpl#getProxyCon <em>Proxy Con</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AImpl extends MinimalEObjectImpl.Container implements A {
	/**
	 * The cached value of the '{@link #getNoProxyNoCon() <em>No Proxy No Con</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoProxyNoCon()
	 * @generated
	 * @ordered
	 */
	protected A noProxyNoCon;

	/**
	 * The cached value of the '{@link #getNoProxyCon() <em>No Proxy Con</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoProxyCon()
	 * @generated
	 * @ordered
	 */
	protected A noProxyCon;

	/**
	 * The cached value of the '{@link #getProxyNoCon() <em>Proxy No Con</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProxyNoCon()
	 * @generated
	 * @ordered
	 */
	protected A proxyNoCon;

	/**
	 * The cached value of the '{@link #getProxyCon() <em>Proxy Con</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProxyCon()
	 * @generated
	 * @ordered
	 */
	protected A proxyCon;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProxyPackage.Literals.A;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public A getNoProxyNoCon() {
		return noProxyNoCon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNoProxyNoCon(A newNoProxyNoCon) {
		A oldNoProxyNoCon = noProxyNoCon;
		noProxyNoCon = newNoProxyNoCon;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProxyPackage.A__NO_PROXY_NO_CON, oldNoProxyNoCon, noProxyNoCon));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public A getNoProxyCon() {
		return noProxyCon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNoProxyCon(A newNoProxyCon, NotificationChain msgs) {
		A oldNoProxyCon = noProxyCon;
		noProxyCon = newNoProxyCon;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProxyPackage.A__NO_PROXY_CON, oldNoProxyCon, newNoProxyCon);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNoProxyCon(A newNoProxyCon) {
		if (newNoProxyCon != noProxyCon) {
			NotificationChain msgs = null;
			if (noProxyCon != null)
				msgs = ((InternalEObject)noProxyCon).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ProxyPackage.A__NO_PROXY_CON, null, msgs);
			if (newNoProxyCon != null)
				msgs = ((InternalEObject)newNoProxyCon).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ProxyPackage.A__NO_PROXY_CON, null, msgs);
			msgs = basicSetNoProxyCon(newNoProxyCon, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProxyPackage.A__NO_PROXY_CON, newNoProxyCon, newNoProxyCon));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public A getProxyNoCon() {
		if (proxyNoCon != null && proxyNoCon.eIsProxy()) {
			InternalEObject oldProxyNoCon = (InternalEObject)proxyNoCon;
			proxyNoCon = (A)eResolveProxy(oldProxyNoCon);
			if (proxyNoCon != oldProxyNoCon) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ProxyPackage.A__PROXY_NO_CON, oldProxyNoCon, proxyNoCon));
			}
		}
		return proxyNoCon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public A basicGetProxyNoCon() {
		return proxyNoCon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProxyNoCon(A newProxyNoCon) {
		A oldProxyNoCon = proxyNoCon;
		proxyNoCon = newProxyNoCon;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProxyPackage.A__PROXY_NO_CON, oldProxyNoCon, proxyNoCon));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public A getProxyCon() {
		return proxyCon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProxyCon(A newProxyCon, NotificationChain msgs) {
		A oldProxyCon = proxyCon;
		proxyCon = newProxyCon;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ProxyPackage.A__PROXY_CON, oldProxyCon, newProxyCon);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProxyCon(A newProxyCon) {
		if (newProxyCon != proxyCon) {
			NotificationChain msgs = null;
			if (proxyCon != null)
				msgs = ((InternalEObject)proxyCon).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ProxyPackage.A__PROXY_CON, null, msgs);
			if (newProxyCon != null)
				msgs = ((InternalEObject)newProxyCon).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ProxyPackage.A__PROXY_CON, null, msgs);
			msgs = basicSetProxyCon(newProxyCon, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProxyPackage.A__PROXY_CON, newProxyCon, newProxyCon));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ProxyPackage.A__NO_PROXY_CON:
				return basicSetNoProxyCon(null, msgs);
			case ProxyPackage.A__PROXY_CON:
				return basicSetProxyCon(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ProxyPackage.A__NO_PROXY_NO_CON:
				return getNoProxyNoCon();
			case ProxyPackage.A__NO_PROXY_CON:
				return getNoProxyCon();
			case ProxyPackage.A__PROXY_NO_CON:
				if (resolve) return getProxyNoCon();
				return basicGetProxyNoCon();
			case ProxyPackage.A__PROXY_CON:
				return getProxyCon();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ProxyPackage.A__NO_PROXY_NO_CON:
				setNoProxyNoCon((A)newValue);
				return;
			case ProxyPackage.A__NO_PROXY_CON:
				setNoProxyCon((A)newValue);
				return;
			case ProxyPackage.A__PROXY_NO_CON:
				setProxyNoCon((A)newValue);
				return;
			case ProxyPackage.A__PROXY_CON:
				setProxyCon((A)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ProxyPackage.A__NO_PROXY_NO_CON:
				setNoProxyNoCon((A)null);
				return;
			case ProxyPackage.A__NO_PROXY_CON:
				setNoProxyCon((A)null);
				return;
			case ProxyPackage.A__PROXY_NO_CON:
				setProxyNoCon((A)null);
				return;
			case ProxyPackage.A__PROXY_CON:
				setProxyCon((A)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ProxyPackage.A__NO_PROXY_NO_CON:
				return noProxyNoCon != null;
			case ProxyPackage.A__NO_PROXY_CON:
				return noProxyCon != null;
			case ProxyPackage.A__PROXY_NO_CON:
				return proxyNoCon != null;
			case ProxyPackage.A__PROXY_CON:
				return proxyCon != null;
		}
		return super.eIsSet(featureID);
	}

} //AImpl
