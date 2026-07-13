import { useState, useEffect } from "react";
import { residentService, type ResidentContact } from "@/services/resident/residentService";
import { Phone, Mail, MapPin, User, CheckCircle2, ShieldCheck, AlertCircle } from "lucide-react";

interface ContactTabProps {
  residentId: string;
}

export const ContactTab = ({ residentId }: ContactTabProps) => {
  const [contacts, setContacts] = useState<ResidentContact[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchContacts = async () => {
      if (!residentId) return;
      try {
        setLoading(true);
        const data = await residentService.getResidentContacts(residentId);
        setContacts(data);
      } catch (err) {
        console.error("Failed to load resident contacts:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchContacts();
  }, [residentId]);

  if (loading) {
    return (
      <div className="flex items-center justify-center p-8">
        <div className="text-slate-400 dark:text-slate-500 animate-pulse text-sm font-medium">
          Loading contacts...
        </div>
      </div>
    );
  }

  if (contacts.length === 0) {
    return (
      <div className="text-slate-400 dark:text-slate-500 p-8 text-center text-sm">
        No contacts registered for this resident.
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      {contacts.map((contact) => (
        <div 
          key={contact.id} 
          className="bg-white dark:bg-slate-900 border border-slate-200/80 dark:border-slate-800 rounded-xl p-5 shadow-xs space-y-4 hover:shadow-md transition-all duration-250 flex flex-col justify-between"
        >
          <div className="space-y-3">
            {/* Header: Name and Relationship */}
            <div className="flex items-start justify-between border-b border-slate-100 dark:border-slate-800 pb-3">
              <div className="flex items-center gap-2">
                <div className="w-10 h-10 bg-slate-100 dark:bg-slate-850 rounded-lg flex items-center justify-center font-bold text-slate-700 dark:text-slate-300">
                  {contact.firstName[0]}{contact.lastName[0]}
                </div>
                <div>
                  <h4 className="font-bold text-base text-slate-850 dark:text-white">
                    {contact.firstName} {contact.middleName ? contact.middleName + ' ' : ''}{contact.lastName}
                  </h4>
                  <span className="text-xs text-slate-400 dark:text-slate-500 font-semibold uppercase tracking-wider">
                    {contact.relationshipType}
                  </span>
                </div>
              </div>
              
              {/* Badges Container */}
              <div className="flex flex-col items-end gap-1">
                {contact.isPrimary && (
                  <span className="inline-flex items-center gap-1 text-[10px] font-bold px-2 py-0.5 rounded-md bg-blue-50 dark:bg-blue-950/20 text-blue-700 dark:text-blue-400 border border-blue-100 dark:border-blue-900/30">
                    <CheckCircle2 className="w-3 h-3" />
                    Primary
                  </span>
                )}
                {contact.isEmergencyContact && (
                  <span className="inline-flex items-center gap-1 text-[10px] font-bold px-2 py-0.5 rounded-md bg-rose-50 dark:bg-rose-950/20 text-rose-700 dark:text-rose-400 border border-rose-100 dark:border-rose-900/30">
                    <AlertCircle className="w-3 h-3" />
                    Emergency
                  </span>
                )}
                {contact.isGuarantor && (
                  <span className="inline-flex items-center gap-1 text-[10px] font-bold px-2 py-0.5 rounded-md bg-emerald-50 dark:bg-emerald-950/20 text-emerald-700 dark:text-emerald-400 border border-emerald-100 dark:border-emerald-900/30">
                    <ShieldCheck className="w-3 h-3" />
                    Guarantor
                  </span>
                )}
              </div>
            </div>

            {/* Info details */}
            <div className="space-y-2 text-sm text-slate-650 dark:text-slate-350">
              <div className="flex items-center gap-2">
                <Phone className="w-4 h-4 text-slate-450 dark:text-slate-500 shrink-0" />
                <span>Primary: <strong className="text-slate-800 dark:text-slate-200">{contact.phonePrimary}</strong></span>
              </div>
              {contact.phoneSecondary && (
                <div className="flex items-center gap-2">
                  <Phone className="w-4 h-4 text-slate-450 dark:text-slate-500 shrink-0" />
                  <span>Secondary: <strong className="text-slate-800 dark:text-slate-200">{contact.phoneSecondary}</strong></span>
                </div>
              )}
              {contact.email && (
                <div className="flex items-center gap-2">
                  <Mail className="w-4 h-4 text-slate-450 dark:text-slate-500 shrink-0" />
                  <a href={`mailto:${contact.email}`} className="text-primary hover:underline font-medium">
                    {contact.email}
                  </a>
                </div>
              )}
              {contact.address && (
                <div className="flex items-start gap-2 pt-1 border-t border-slate-100/50 dark:border-slate-800/40 mt-1">
                  <MapPin className="w-4 h-4 text-slate-450 dark:text-slate-500 shrink-0 mt-0.5" />
                  <span className="text-xs">
                    {contact.address.streetLine1}
                    {contact.address.streetLine2 ? `, ${contact.address.streetLine2}` : ""}, {contact.address.city}, {contact.address.state} {contact.address.zipCode}
                  </span>
                </div>
              )}
            </div>
          </div>

          {/* Financial Responsibility Progress */}
          {contact.financialResponsibilityPct !== undefined && contact.financialResponsibilityPct > 0 && (
            <div className="mt-4 pt-3 border-t border-slate-100 dark:border-slate-800 text-xs">
              <div className="flex justify-between font-semibold text-slate-600 dark:text-slate-400 mb-1">
                <span>Financial Responsibility</span>
                <span>{contact.financialResponsibilityPct}%</span>
              </div>
              <div className="w-full h-1.5 bg-slate-100 dark:bg-slate-800 rounded-full overflow-hidden">
                <div 
                  className="h-full bg-emerald-500 rounded-full" 
                  style={{ width: `${contact.financialResponsibilityPct}%` }}
                />
              </div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};