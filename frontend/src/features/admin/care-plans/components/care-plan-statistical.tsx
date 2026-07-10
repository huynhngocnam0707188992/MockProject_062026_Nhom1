import { AlarmClock, Clock, Database, Paperclip } from "lucide-react";
import Card from "../ui/card";

export default function CarePlanStatistical() {
  return (
    <div className="flex gap-3 flex-wrap">
      <Card
        icon={Database}
        title="Total plans"
        amount="24"
        className="bg-blue-200"
        width="basis-[calc((100%-36px)/4)]"
        height="h-[120px]"
      ></Card>

      <Card
        icon={AlarmClock}
        title="Total plans"
        amount="24"
        className="bg-blue-200"
        width="basis-[calc((100%-36px)/4)]"
        height="h-[120px]"
      ></Card>
      <Card
        icon={Clock}
        title="Total plans"
        amount="24"
        width="basis-[calc((100%-36px)/4)]"
        className="bg-blue-200"
        height="h-[120px]"
      ></Card>
      <Card
        icon={Paperclip}
        title="Total plans"
        amount="24"
        className="bg-blue-200"
        height="h-[120px]"
        width="basis-[calc((100%-36px)/4)]"
      ></Card>
    </div>
  );
}
