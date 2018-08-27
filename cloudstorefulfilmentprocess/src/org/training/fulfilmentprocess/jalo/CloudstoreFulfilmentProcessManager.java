/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package org.training.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.training.fulfilmentprocess.constants.CloudstoreFulfilmentProcessConstants;

public class CloudstoreFulfilmentProcessManager extends GeneratedCloudstoreFulfilmentProcessManager
{
	public static final CloudstoreFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (CloudstoreFulfilmentProcessManager) em.getExtension(CloudstoreFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
