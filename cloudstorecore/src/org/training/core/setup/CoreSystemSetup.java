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
package org.training.core.setup;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import org.training.core.constants.CloudstoreCoreConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides hooks into the system's initialization and update processes.
 */
@SystemSetup(extension = CloudstoreCoreConstants.EXTENSIONNAME)
public class CoreSystemSetup extends AbstractSystemSetup
{
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		importImpexFile(context, "/cloudstorecore/import/common/essential-data.impex");
		importImpexFile(context, "/cloudstorecore/import/common/countries.impex");
		importImpexFile(context, "/cloudstorecore/import/common/delivery-modes.impex");

		importImpexFile(context, "/cloudstorecore/import/common/themes.impex");
		importImpexFile(context, "/cloudstorecore/import/common/user-groups.impex");
		importImpexFile(context, "/cloudstorecore/import/common/cronjobs.impex");
	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<>();

		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));

		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);

		final List<String> extensionNames = getExtensionNames();

		processCockpit(context, importAccessRights, extensionNames, "cmscockpit",
				"/cloudstorecore/import/cockpits/cmscockpit/cmscockpit-users.impex",
				"/cloudstorecore/import/cockpits/cmscockpit/cmscockpit-access-rights.impex");

		processCockpit(context, importAccessRights, extensionNames, "productcockpit",
				"/cloudstorecore/import/cockpits/productcockpit/productcockpit-users.impex",
				"/cloudstorecore/import/cockpits/productcockpit/productcockpit-access-rights.impex",
				"/cloudstorecore/import/cockpits/productcockpit/productcockpit-constraints.impex");

		processCockpit(context, importAccessRights, extensionNames, "cscockpit",
				"/cloudstorecore/import/cockpits/cscockpit/cscockpit-users.impex",
				"/cloudstorecore/import/cockpits/cscockpit/cscockpit-access-rights.impex");

		processCockpit(context, importAccessRights, extensionNames, "reportcockpit",
				"/cloudstorecore/import/cockpits/reportcockpit/reportcockpit-users.impex",
				"/cloudstorecore/import/cockpits/reportcockpit/reportcockpit-access-rights.impex");

		if (extensionNames.contains("mcc"))
		{
			importImpexFile(context, "/cloudstorecore/import/common/mcc-sites-links.impex");
		}
	}

	protected void processCockpit(final SystemSetupContext context, final boolean importAccessRights,
			final List<String> extensionNames, final String cockpit, final String... files)
	{
		if (importAccessRights && extensionNames.contains(cockpit))
		{
			for (final String file : files)
			{
				importImpexFile(context, file);
			}
		}
	}

	protected List<String> getExtensionNames()
	{
		return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
	}

	protected <T> T getBeanForName(final String name)
	{
		return (T) Registry.getApplicationContext().getBean(name);
	}
}
