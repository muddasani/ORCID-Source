<#--

    =============================================================================

    ORCID (R) Open Source
    http://orcid.org

    Copyright (c) 2012-2014 ORCID, Inc.
    Licensed under an MIT-Style License (MIT)
    http://orcid.org/open-source-license

    This copyright and license information (including a link to the full license)
    shall be included in its entirety in all copies or substantial portion of
    the software.

    =============================================================================

-->
<@public nav="manage-consortium">
    <div class="row" ng-controller="externalConsortiumCtrl">
        <div class="col-md-3 lhs col-sm-12 col-xs-12 padding-fix">
            <#include "includes/id_banner.ftl"/>
        </div>
        <div class="col-md-9 col-sm-12 col-xs-12 manage-consortium">
            <h1 id="manage-consortium-lead"><@spring.message "manage_consortium.manage_consortium"/></h1>
            <p><@spring.message "manage_consortium.manage_consortium_text_1"/>
            	<a href="<@orcid.rootPath '/members'/>" target="_blank"><@spring.message "manage_consortium.member_list_link"/></a>
            	<@spring.message "manage_consortium.manage_consortium_text_2"/>
            	<a href="mailto:<@spring.message "manage_consortium.support_email"/>"><@spring.message "manage_consortium.support_email"/></a></p>
            <div ng-show="consortium != null" ng-cloak>
                <div>
                	<h2 id="manage-consortium-lead"><@spring.message "manage_consortium.consortium_lead"/></h2>
                    <!-- Name -->
                    <div class="row">
                        <div class="col-md-9 col-sm-12 col-xs-12">
                            <label><@orcid.msg 'manage_consortium.org_name'/></label>
                            <input type="text" ng-model="consortium.name.value" class="full-width-input" />
                            <span class="orcid-error" ng-show="consortium.name.errors.length > 0">
                                <div ng-repeat='error in consortium.name.errors' ng-bind-html="error"></div>
                            </span>
                        </div>
                    </div>
                    <!-- website -->
                    <div class="row">
                        <div class="col-md-9 col-sm-12 col-xs-12">
                            <label><@orcid.msg 'manage_consortium.website'/></label>
                            <input type="text" ng-model="consortium.website.value" class="full-width-input" />
                            <span class="orcid-error" ng-show="consortium.website.errors.length > 0">
                                <div ng-repeat='error in consortium.website.errors' ng-bind-html="error"></div>
                            </span>
                        </div>
                    </div>                     
                </div>
                <div>
                    <h3>
                        <@spring.message "manage_consortium.contacts_heading"/>
                    </h3>
                    <p>
                        <@spring.message "manage_consortium.contacts_text"/>
                    </p>
                    <table>
                        <thead>
                            <tr>
                                <th>Name</th><th>Email</th><th>Main contact</th><th>Technical contact</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="contact in consortium.contactsList">
                                <td>{{contact.name}}</td>
                                <td>{{contact.email}}</td>
                                <td><input type="checkbox" ng-model="contact.mainContact" ng-change="update(contact)"></input></td>
                                <td><input type="checkbox" ng-model="contact.technicalContact" ng-change="update(contact)"></input></td>
                                <td class="tooltip-container">
                                    <a id="revokeAppBtn" name="{{contact.email}}" ng-click="confirmRevoke(contact)"
                                        class="glyphicon glyphicon-trash grey">
                                        <div class="popover popover-tooltip top">
                                            <div class="arrow"></div>
                                            <div class="popover-content">
                                                <span><@spring.message "manage_consortium.remove_contact"/></span>
                                            </div>
                                        </div>
                                    </a>
                                </td>
		    				</td>
                            </tr>
                        </tbody>
                    </table>
                    <div>
                    	<h3>
                        	<@spring.message "manage_consortium.add_contacts_heading"/>
                    	</h3>
                    	<p>
                    		<@spring.message "manage_consortium.add_contacts_search_for"/>
                    	</p>
                        <form ng-submit="search()">
                            <input type="text" placeholder="Email address" class="input-xlarge inline-input" ng-model="input.text"></input>
                            <input type="submit" class="btn btn-primary" value="Search"></input>
                        </form>
                    </div>
                    <div>
	                    <table class="ng-cloak table" ng-show="areResults()">
	                        <thead>
	                            <tr>
	                                <th>${springMacroRequestContext.getMessage("manage.thproxy")}</th>
	                                <th>${springMacroRequestContext.getMessage("search_results.thORCIDID")}</th>
	                                <th></th>
	                            </tr>
	                        </thead>
	                        <tbody>
	                            <tr ng-repeat='result in results' class="new-search-result">
	                                <td><a href="{{result['orcid-profile']['orcid-identifier'].uri}}" target="_blank" ng-bind="getDisplayName(result)"></a></td>
	                                <td class='search-result-orcid-id'><a href="{{result['orcid-profile']['orcid-identifier'].uri}}" target="_blank">{{result['orcid-profile']['orcid-identifier'].path}}</td>
	                                <td>
	                                    <span ng-show="effectiveUserOrcid !== result['orcid-profile']['orcid-identifier'].path">
	                                        <span ng-show="!contactsByOrcid[result['orcid-profile']['orcid-identifier'].path]"
	                                            ng-click="confirmAddContact(result['orcid-profile']['orcid-bio']['personal-details']['given-names'].value + ' ' + result['orcid-profile']['orcid-bio']['personal-details']['family-name'].value, result['orcid-profile']['orcid-identifier'].path, $index)"
	                                            class="btn btn-primary">${springMacroRequestContext.getMessage("manage.spanadd")}</span>
	                                        <a ng-show="contactsByOrcid[result['orcid-profile']['orcid-identifier'].path]"
	                                            ng-click="confirmRevoke(result['orcid-profile']['orcid-bio']['personal-details']['given-names'].value + ' ' + result['orcid-profile']['orcid-bio']['personal-details']['family-name'].value, result['orcid-profile']['orcid-identifier'].path, $index)"
	                                            class="glyphicon glyphicon-trash grey"
	                                            title="${springMacroRequestContext.getMessage("manage.revokeaccess")}"></a>
	                                    </span>
	                                    <span ng-show="effectiveUserOrcid === result['orcid-profile']['orcid-identifier'].path">${springMacroRequestContext.getMessage("manage_delegation.you")}</span>
	                                </td>
	                            </tr>
	                        </tbody>
	                    </table>
	                    <div id="show-more-button-container">
	                        <button id="show-more-button" type="submit" class="ng-cloak btn" ng-click="getMoreResults()" ng-show="areMoreResults">Show more</button>
	                        <span id="ajax-loader" class="ng-cloak" ng-show="showLoader"><i class="glyphicon glyphicon-refresh spin x2 green"></i></span>
	                    </div>
                    </div>
                <div id="no-results-alert" class="orcid-hide alert alert-error no-contact-matches"><@spring.message "orcid.frontend.web.no_results"/></div>
                </div>
                <!-- Buttons -->
                <div class="row">
                    <div class="controls save-btns col-md-12 col-sm-12 col-xs-12">
                        <span id="bottom-confirm-update-consortium" ng-click="confirmUpdateConsortium()" class="btn btn-primary"><@orcid.msg 'freemarker.btnsaveallchanges'/></span>
                    </div>
                </div> 
            </div>
        </div>
    </div>
    <script type="text/ng-template" id="confirm-modal-consortium">
        <div class="lightbox-container">
            <div class="row">
                <div class="col-md-12 col-xs-12 col-sm-12">
                    <h3><@orcid.msg 'manage_member.edit_member.confirm_update.title' /></h3>    
                    <p><@orcid.msg 'manage_member.edit_memeber.confirm_update.text' /></p>          
                    <p><strong>{{member.groupName.value}}</strong></p>                      
                    <div class="btn btn-danger" ng-click="updateConsortium()">
                        <@orcid.msg 'manage_member.edit_member.btn.update' />
                    </div>
                    <a href="" ng-click="closeModal()"><@orcid.msg 'freemarker.btncancel' /></a>
                </div>
            </div>
        </div>
    </script>
    <script type="text/ng-template" id="confirm-add-contact-modal">
	    <div class="lightbox-container">
	       <h3><@orcid.msg 'manage_consortium.add_contacts_confirm_heading'/></h3>
	       <div ng-show="effectiveUserOrcid === contactToAdd">
	          <p class="alert alert-error"><@orcid.msg 'manage_delegation.youcantaddyourself'/></p>
	          <a href="" ng-click="closeModal()"><@orcid.msg 'freemarker.btnclose'/></a>
	       </div>
	       <div ng-hide="effectiveUserOrcid === contactToAdd">
	       		
	          <p>{{contactNameToAdd}} ({{contactToAdd}})</p>
	          <form ng-submit="addContact()">
	              <button class="btn btn-primary" ><@orcid.msg 'manage.spanadd'/></button>
	              <a href="" ng-click="closeModal()"><@orcid.msg 'freemarker.btncancel'/></a>
	          </form>
	       </div>
	       <div ng-show="errors.length === 0">
	           <br></br>
	       </div>
	    </div>
	</script>
	
	<script type="text/ng-template" id="confirm-add-contact-by-email-modal">
	    <div class="lightbox-container">
	        <h3><@orcid.msg 'manage_consortium.add_contacts_confirm_heading'/></h3>
	        <div ng-show="!emailSearchResult.found" >
	            <p class="alert alert-error"><@orcid.msg 'manage_delegation.sorrynoaccount1'/>{{input.text}}<@orcid.msg 'manage_delegation.sorrynoaccount2'/></p>
	            <p><@orcid.msg 'manage_consortium.add_contacts_must_have_account'/></p>
	            <a href="" ng-click="closeModal()"><@orcid.msg 'freemarker.btnclose'/></a>
	        </div>
	        <div ng-show="emailSearchResult.found">
	            <p>{{input.text}}</p>
	            <form ng-submit="addContactByEmail(input.text)">
	                <button class="btn btn-primary"><@orcid.msg 'manage.spanadd'/></button>
	                <a href="" ng-click="closeModal()" class="cancel-option"><@orcid.msg 'freemarker.btncancel'/></a>
	            </form>
	        </div>
	    </div>
    </script>
    
    <script type="text/ng-template" id="revoke-contact-modal">
	    <div class="lightbox-container">
	        <h3><@orcid.msg 'manage_consortium.remove_contact_confirm_heading'/></h3>
	        <p> {{contactToRevoke.name}} ({{contactToRevoke.id}})</p>
	        <form ng-submit="revoke(contactToRevoke)">
	            <button class="btn btn-danger"><@orcid.msg 'manage_consortium.remove_contact_confirm_btn'/></button>
	            <a href="" ng-click="closeModal()" class="cancel-option"><@orcid.msg 'freemarker.btncancel'/></a>
	        </form>
	        <div ng-show="errors.length === 0">
	            <br></br>
	        </div>
	    </div>
    </script>

</@public>
