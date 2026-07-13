import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Field, FieldGroup } from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import Text from "../../ui/Text";
import Title from "../../ui/Title";

export default function ApproveCarePlanDialog() {
  return (
    <div>
      <Dialog>
        <form>
          <DialogTrigger
            render={
              <Button
                className={`min-h-14 bg-white  border-2 border-red-300 border-solid text-red-600 w-full`}
              >
                Reject & Return
              </Button>
            }
          />
          <DialogContent className="w-[420px] rounded-xl p-6">
            <DialogHeader className="space-y-2">
              <DialogTitle>
                <Title className="text-[24px] font-bold">
                  Electronic Signature Required
                </Title>
              </DialogTitle>

              <DialogDescription>
                <Text className="text-sm text-gray-500">
                  Approving activates this care plan and generates downstream
                  tasks & billing.
                </Text>
              </DialogDescription>
            </DialogHeader>

            <div className="mt-5 space-y-5">
              {/* User */}
              <div className="flex items-center gap-3 rounded-lg border border-gray-200 p-3">
                <div className="flex h-10 w-10 items-center justify-center rounded-full border text-gray-500">
                  D
                </div>

                <div>
                  <Title className="text-base">Denise Carter, DON</Title>

                  <Text className="text-sm text-gray-500">
                    Signing as Director of Nursing
                  </Text>
                </div>
              </div>

              {/* Password */}
              <Field>
                <Label>Re-enter password to sign</Label>

                <Input type="password" placeholder="••••••••••" />
              </Field>

              {/* Checkbox */}
              <label className="flex items-start gap-2">
                <input type="checkbox" defaultChecked className="mt-1" />

                <Text className="text-sm text-gray-700">
                  I attest this plan is compliant and ready for activation.
                </Text>
              </label>

              {/* Timestamp */}
              <div>
                <Text className="text-xs text-gray-500">
                  Signature timestamp: {new Date().toISOString()}
                </Text>

                <Text className="text-xs text-gray-400">
                  Recorded immutably in audit log.
                </Text>
              </div>
            </div>

            <DialogFooter className="mt-6 flex gap-3">
              <DialogClose
                render={
                  <Button variant="outline" className="flex-1">
                    Cancel
                  </Button>
                }
              />

              <Button type="submit" className="flex-1">
                Sign & Approve
              </Button>
            </DialogFooter>
          </DialogContent>
        </form>
      </Dialog>
    </div>
  );
}
